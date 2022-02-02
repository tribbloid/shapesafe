package shapesafe.core.shape.unary

import shapesafe.core.arity.ArityType
import shapesafe.core.axis.Axis
import shapesafe.core.axis.Axis.:<<-
import shapesafe.core.debugging.{Expressions, Reporters}
import shapesafe.core.shape.StaticShape.><
import shapesafe.core.shape._
import shapesafe.core.{Poly1Base, XString}
import shapesafe.m.viz.VizCTSystem.EmitError
import shapeless.ops.hlist.{At, Reverse}
import shapeless.ops.record.Selector
import shapeless.{HList, Nat, Witness}

case class Select1[
    S1 <: ShapeType,
    I <: Index
](
    s1: S1 with ShapeType,
    index: I
) extends Conjecture1.^[S1] {

  override type Expr = Expressions.Select1[S1#Expr, I#Expr]

  override type _Refute = "Index not found"
}

trait Select1_Imp0 {

  import ProveShape._

  implicit def refute[
      S1 <: ShapeType,
      P1 <: LeafShape,
      I <: Index,
      MSG
  ](
      implicit
      lemma1: S1 |- P1,
      refute0: Reporters.ForShape.Refute0[Select1[P1, I], MSG],
      msg: EmitError[MSG]
  ): Select1[S1, I] |- LeafShape = {
    ???
  }
}

object Select1 extends Select1_Imp0 {

  import ProveShape._

  implicit def simplify[
      S1 <: ShapeType,
      P1 <: StaticShape,
      I <: Index,
      O <: Axis
  ](
      implicit
      lemma1: S1 |- P1,
      lemma2: Premise.=>>[Select1[P1, I], O]
  ): Select1[S1, I] |- (StaticShape.Eye >< O) = {

    ProveShape.forAll[Select1[S1, I]].=>> { v =>
      val p1: P1 = lemma1.instanceFor(v.s1)
      val vv: Select1[P1, I] = v.copy(s1 = p1)

      Shape _and lemma2(vv)
    }
  }

  object Premise extends Poly1Base[Select1[_, _], Axis] {

    implicit def byName[
        P1 <: StaticShape,
        N <: XString,
        A <: ArityType
    ](
        implicit
        _selector: Selector.Aux[P1#Record, N, A]
    ): Select1[P1, Index.Name[N]] =>> (A :<<- N) = {
      forAll[Select1[P1, Index.Name[N]]].=>> { v =>
        val p1: P1 = v.s1

        val arity: A = _selector(p1.record)
        val w: Witness.Aux[N] = v.index.w
        arity.^ :<<- w
      }
    }

    implicit def byLeft[
        P1 <: StaticShape,
        N <: Nat,
        R <: HList,
        S <: Int,
        O <: Axis
    ](
        implicit
        reverse: Reverse.Aux[P1#Static, R],
        _at: At.Aux[R, N, O]
    ): Select1[P1, Index.Left[N, S]] =>> O = {
      forAll[Select1[P1, Index.Left[N, S]]].=>> { v =>
        val p1 = v.s1

        _at(reverse(p1.static))
      }
    }
  }
}
