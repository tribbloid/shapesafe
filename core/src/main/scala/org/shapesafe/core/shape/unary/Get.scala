package org.shapesafe.core.shape.unary

import org.shapesafe.core.Poly1Base
import org.shapesafe.core.arity.Arity
import org.shapesafe.core.axis.Axis
import org.shapesafe.core.axis.Axis.:<<-
import org.shapesafe.core.shape.LeafShape.><
import org.shapesafe.core.shape.{Index, LeafShape, ProveShape, Shape, ShapeConjecture}
import shapeless.ops.hlist.At
import shapeless.ops.record.Selector
import shapeless.{Nat, Witness}

case class Get[ // last step of einsum, contract, transpose, etc.
    S1 <: Shape,
    I <: Index
](
    s1: S1,
    index: I
) extends ShapeConjecture {}

object Get {

//  object Direct extends ProveShape.SubScope
  //  import Direct._
  import ProveShape._
  import Factory._

  implicit def simplify[
      S1 <: Shape,
      P1 <: LeafShape,
      I <: Index,
      O <: Axis
  ](
      implicit
      lemma1: |-<[S1, P1],
      lemma2: Premise.==>[Get[P1, I], O]
  ): Get[S1, I] =>> (LeafShape.Eye >< O) = {

    ProveShape.forAll[Get[S1, I]].=>> { v =>
      val p1: P1 = lemma1.valueOf(v.s1)
      val vv: Get[P1, I] = v.copy(s1 = p1)

      LeafShape.Eye >|< lemma2(vv)
    }
  }

  object Premise extends Poly1Base[Get[_, _], Axis] {

    implicit def byName[
        P1 <: LeafShape,
        N <: String,
        A <: Arity
    ](
        implicit
        _selector: Selector.Aux[P1#Record, N, A]
    ): Get[P1, Index.Name[N]] ==> (A :<<- N) = {
      forAll[Get[P1, Index.Name[N]]].==> { v =>
        val p1: P1 = v.s1

        val arity = _selector(p1.record)
        val w: Witness.Aux[N] = v.index.w
        arity :<<- w
      }
    }

    implicit def byII[
        P1 <: LeafShape,
        N <: Nat,
        O <: Axis
    ](
        implicit
        _at: At.Aux[P1#Static, N, O]
    ): Get[P1, Index.I_th[N]] ==> O = {
      forAll[Get[P1, Index.I_th[N]]].==> { v =>
        val p1 = v.s1

        _at(p1.static)
      }
    }
  }
}
