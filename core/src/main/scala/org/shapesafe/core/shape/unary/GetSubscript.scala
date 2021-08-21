package org.shapesafe.core.shape.unary

import org.shapesafe.core.{Poly1Base, XString}
import org.shapesafe.core.arity.Arity
import org.shapesafe.core.axis.Axis
import org.shapesafe.core.axis.Axis.:<<-
import org.shapesafe.core.debugging.Expressions.Expr
import org.shapesafe.core.debugging.{Expressions, OpStrs, Reporters}
import org.shapesafe.core.shape.StaticShape.><
import org.shapesafe.core.shape._
import org.shapesafe.m.viz.VizCTSystem.EmitError
import shapeless.ops.hlist.At
import shapeless.ops.record.Selector
import shapeless.{Nat, Witness}

case class GetSubscript[ // last step of einsum, contract, transpose, etc.
    S1 <: Shape,
    I <: Index
](
    s1: S1 with Shape,
    index: I
) extends Conjecture1.^[S1] {

  override type _AsOpStr = OpStrs.Infix[S1, " GetSubscript ", I]
  override type _AsExpr = Expressions.GetSubscript[Expr[S1], Expr[I]]

  override type _Refute = "Index not found"
}

trait GetSubscript_Imp0 {

  import ProveShape._
  import ForAll._

  implicit def refute[
      S1 <: Shape,
      P1 <: LeafShape,
      I <: Index,
      MSG
  ](
      implicit
      lemma1: S1 |- P1,
      refute0: Reporters.ForShape.Refute0[GetSubscript[P1, I], MSG],
      msg: EmitError[MSG]
  ): GetSubscript[S1, I] =>> LeafShape = {
    ???
  }
}

object GetSubscript extends GetSubscript_Imp0 {

  import ProveShape._
  import ForAll._

  implicit def simplify[
      S1 <: Shape,
      P1 <: StaticShape,
      I <: Index,
      O <: Axis
  ](
      implicit
      lemma1: S1 |- P1,
      lemma2: Premise.==>[GetSubscript[P1, I], O]
  ): GetSubscript[S1, I] =>> (StaticShape.Eye >< O) = {

    ProveShape.forAll[GetSubscript[S1, I]].=>> { v =>
      val p1: P1 = lemma1.valueOf(v.s1)
      val vv: GetSubscript[P1, I] = v.copy(s1 = p1)

      Shape appendInner lemma2(vv)
    }
  }

  object Premise extends Poly1Base[GetSubscript[_, _], Axis] {

    implicit def byName[
        P1 <: StaticShape,
        N <: XString,
        A <: Arity
    ](
        implicit
        _selector: Selector.Aux[P1#Record, N, A]
    ): GetSubscript[P1, Index.Name[N]] ==> (A :<<- N) = {
      forAll[GetSubscript[P1, Index.Name[N]]].==> { v =>
        val p1: P1 = v.s1

        val arity: A = _selector(p1.record)
        val w: Witness.Aux[N] = v.index.w
        arity.^ :<<- w
      }
    }

    implicit def byIndex[
        P1 <: StaticShape,
        N <: Nat,
        O <: Axis
    ](
        implicit
        _at: At.Aux[P1#Static, N, O]
    ): GetSubscript[P1, Index.I_th[N]] ==> O = {
      forAll[GetSubscript[P1, Index.I_th[N]]].==> { v =>
        val p1 = v.s1

        _at(p1.static)
      }
    }
  }
}
