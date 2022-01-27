package shapesafe.core.shape.unary

import shapesafe.core.arity.ArityType
import shapesafe.core.axis.Axis
import shapesafe.core.axis.Axis.:<<-
import shapesafe.core.debugging.{Expressions, Reporters}
import shapesafe.core.shape.StaticShape.><
import shapesafe.core.shape._
import shapesafe.core.{Poly1Base, XString}
import shapesafe.m.viz.VizCTSystem.EmitError
import shapeless.ops.hlist.At
import shapeless.ops.record.Selector
import shapeless.{Nat, Witness}

case class GetSubscript[ // last step of einsum, contract, transpose, etc.
    S1 <: ShapeType,
    I <: Index
](
    s1: S1 with ShapeType,
    index: I
) extends Conjecture1.^[S1] {

  override type Expr = Expressions.GetSubscript[S1#Expr, I#Expr]

  override type _Refute = "Index not found"
}

trait GetSubscript_Imp0 {

  import ProveShape._

  implicit def refute[
      S1 <: ShapeType,
      P1 <: LeafShape,
      I <: Index,
      MSG
  ](
      implicit
      lemma1: S1 |- P1,
      refute0: Reporters.ForShape.Refute0[GetSubscript[P1, I], MSG],
      msg: EmitError[MSG]
  ): GetSubscript[S1, I] |- LeafShape = {
    ???
  }
}

object GetSubscript extends GetSubscript_Imp0 {

  import ProveShape._

  implicit def simplify[
      S1 <: ShapeType,
      P1 <: StaticShape,
      I <: Index,
      O <: Axis
  ](
      implicit
      lemma1: S1 |- P1,
      lemma2: Premise.=>>[GetSubscript[P1, I], O]
  ): GetSubscript[S1, I] |- (StaticShape.Eye >< O) = {

    ProveShape.forAll[GetSubscript[S1, I]].=>> { v =>
      val p1: P1 = lemma1.instanceFor(v.s1)
      val vv: GetSubscript[P1, I] = v.copy(s1 = p1)

      Shape _and lemma2(vv)
    }
  }

  object Premise extends Poly1Base[GetSubscript[_, _], Axis] {

    implicit def byName[
        P1 <: StaticShape,
        N <: XString,
        A <: ArityType
    ](
        implicit
        _selector: Selector.Aux[P1#Record, N, A]
    ): GetSubscript[P1, Index.Name[N]] =>> (A :<<- N) = {
      forAll[GetSubscript[P1, Index.Name[N]]].=>> { v =>
        val p1: P1 = v.s1

        val arity: A = _selector(p1.record)
        val w: Witness.Aux[N] = v.index.w
        arity.^ :<<- w
      }
    }

    implicit def byIndex[
        P1 <: StaticShape,
        N <: Nat,
        S <: Int,
        O <: Axis
    ](
        implicit
        _at: At.Aux[P1#Static, N, O]
    ): GetSubscript[P1, Index.I_th[N, S]] =>> O = {
      forAll[GetSubscript[P1, Index.I_th[N, S]]].=>> { v =>
        val p1 = v.s1

        _at(p1.static)
      }
    }
  }
}
