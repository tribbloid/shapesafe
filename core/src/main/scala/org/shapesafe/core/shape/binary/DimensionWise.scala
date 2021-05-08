package org.shapesafe.core.shape.binary

import com.tribbloids.graph.commons.util.HasOuter
import org.shapesafe.core.arity.Arity
import org.shapesafe.core.arity.binary.Op2Like
import org.shapesafe.core.debugging.OpsUtil.Peek
import org.shapesafe.core.debugging.symbol
import org.shapesafe.core.shape.unary.UnaryIndexingFn
import org.shapesafe.core.shape.{LeafShape, ProveShape, Shape}
import shapeless.ops.hlist.Zip
import shapeless.{::, HList, HNil}

trait DimensionWise {

  val op: Op2Like
  type _Binary <: symbol.Binary

  // all names must be distinctive - no duplication allowed
  trait _On[
      S1 <: Shape,
      S2 <: Shape
  ] extends Conjecture2.^[S1, S2]
      with HasOuter {

    override def outer: DimensionWise.this.type = DimensionWise.this

    def s1: S1 with Shape
    def s2: S2 with Shape

    override type _Ops = Peek.PrefixW2[_Binary#Lit, S1, S2]
    override type _Ovrd = _Binary#On[S1#Ovrd, S2#Ovrd]
  }

  object _On {

    import ProveShape.Factory._
    import ProveShape.|-

    implicit def simplify[
        S1 <: Shape,
        S2 <: Shape,
        P1 <: LeafShape,
        P2 <: LeafShape,
        HO <: HList
    ](
        implicit
        lemma1: S1 |- P1,
        lemma2: S2 |- P2,
        zip: Zip.Aux[P1#_Dimensions#Static :: P2#_Dimensions#Static :: HNil, HO],
        toShape: _Indexing.ToShape.Case[HO]
    ): _On[S1, S2] =>> toShape.Out = {

      ProveShape.forAll[_On[S1, S2]].=>> { v =>
        val p1 = lemma1.valueOf(v.s1)
        val p2 = lemma2.valueOf(v.s2)
        val zipped = zip.apply(p1.dimensions.static :: p2.dimensions.static :: HNil)

        val result = toShape.apply(zipped)
        result
      }
    }
  }

  case class On[
      S1 <: Shape,
      S2 <: Shape
  ](
      override val s1: S1 with Shape,
      override val s2: S2 with Shape
  ) extends _On[S1, S2] {}

  // TODO: now sure if it is too convoluted, should it extends BinaryIndexingFn?
  object _Indexing extends UnaryIndexingFn {

    import org.shapesafe.core.arity.ProveArity.|-

    implicit def cons[
        TI <: HList,
        TO <: HList,
        A1 <: Arity,
        A2 <: Arity,
        AO <: Arity
    ](
        implicit
        consTail: TI ==> TO,
        proveArity: op.On[A1, A2] |- AO
    ): ((A1, A2) :: TI) ==> (AO :: TO) = {
      forAll[(A1, A2) :: TI].==> { v =>
        val ti = v.tail
        val to = consTail(ti)

        val hi = v.head
        val ho = proveArity.valueOf(op.on(hi._1.^, hi._2.^))

        ho :: to
      }
    }
  }
}
