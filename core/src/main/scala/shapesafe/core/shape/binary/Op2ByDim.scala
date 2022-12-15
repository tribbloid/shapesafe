package shapesafe.core.shape.binary

import ai.acyclic.prover.commons.HasOuter
import shapeless.ops.hlist.Zip
import shapeless.{::, HList, HNil}
import shapesafe.core.arity.ArityType
import shapesafe.core.arity.binary.Op2Like
import shapesafe.core.debugging.{NotationsLike, Refutes}
import shapesafe.core.shape.unary.RecordLemma
import shapesafe.core.shape.{ProveShape, ShapeType, StaticShape}

trait Op2ByDim {

  val op: Op2Like

  type Condition[_ <: StaticShape, _ <: StaticShape]

  type _NotationProto <: NotationsLike.Proto2

  type _RefuteProto

  trait _On[
      S1 <: ShapeType,
      S2 <: ShapeType
  ] extends Conjecture2.^[S1, S2]
      with HasOuter {

    override def outer: Op2ByDim.this.type = Op2ByDim.this

    final override type Notation = _NotationProto#Apply[S1, S2]

    final override type _RefuteTxt = _RefuteProto

    def s1: S1
    def s2: S2
  }

  object _On {

    import ProveShape._

    implicit def simplify[
        S1 <: ShapeType,
        S2 <: ShapeType,
        P1 <: StaticShape,
        P2 <: StaticShape,
        HO <: HList
    ](
        implicit
        lemma1: S1 |- P1,
        lemma2: S2 |- P2,
        condition: Refutes.ForShape.NotFoundInfo[Condition[P1, P2], _On[P1, P2]],
        zip: Zip.Aux[P1#_Dimensions#Static :: P2#_Dimensions#Static :: HNil, HO],
        // TODO: no need, can define Indexing directly
        toShape: _Lemma.ToShape.Case[HO]
    ): _On[S1, S2] |- toShape.Out = {

      ProveShape.forAll[_On[S1, S2]].=>> { v =>
        val p1 = lemma1.instanceFor(v.s1)
        val p2 = lemma2.instanceFor(v.s2)
        val zipped = zip.apply(p1.dimensions.static :: p2.dimensions.static :: HNil)

        val result = toShape.apply(zipped)
        result
      }
    }
  }

  case class On[
      S1 <: ShapeType,
      S2 <: ShapeType
  ](
      override val s1: S1,
      override val s2: S2
  ) extends _On[S1, S2] {}

  // TODO: now sure if it is too convoluted, should it extends BinaryIndexingFn?
  object _Lemma extends RecordLemma {

    import shapesafe.core.arity.ProveArity.|-

    implicit def cons[
        TI <: HList,
        TO <: HList,
        A1 <: ArityType,
        A2 <: ArityType,
        AO <: ArityType
    ](
        implicit
        consTail: TI =>> TO,
        proveArity: op.On[A1, A2] |- AO
    ): ((A1, A2) :: TI) =>> (AO :: TO) = {
      forAll[(A1, A2) :: TI].=>> { v =>
        val ti = v.tail
        val to = consTail(ti)

        val hi = v.head
        val ho = proveArity.instanceFor(op.on(hi._1.^, hi._2.^))

        ho :: to
      }
    }
  }
}
