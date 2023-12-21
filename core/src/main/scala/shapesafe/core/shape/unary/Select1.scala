package shapesafe.core.shape.unary

import shapeless.ops.hlist.{At, Reverse}
import shapeless.ops.record.Selector
import shapeless.{HList, Nat, Witness}
import shapesafe.core.arity.ArityType
import shapesafe.core.axis.Axis
import shapesafe.core.axis.Axis.:<<-
import shapesafe.core.debugging.Notations
import shapesafe.core.shape.StaticShape.><
import shapesafe.core.shape._
import shapesafe.core.{AdHocPoly1, XString}

case class Select1[
    S1 <: ShapeType,
    I <: Index
](
    s1: S1 with ShapeType,
    index: I
) extends Conjecture1.On[S1] {

  override type Notation = Notations.Select1[S1#Notation, I#Notation]

  override type _RefuteTxt = "Index not found"
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

  object Premise extends AdHocPoly1 {

    implicit def byName[
        P1 <: StaticShape,
        N <: XString,
        A <: ArityType
    ](
        implicit
        _selector: Selector.Aux[P1#Record, N, A]
    ): Select1[P1, Index.Name[N]] =>> (A :<<- N) = {
      at[Select1[P1, Index.Name[N]]].defining { v =>
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
    ): Select1[P1, Index.LtoR[N, S]] =>> O = {
      at[Select1[P1, Index.LtoR[N, S]]].defining { v =>
        val p1 = v.s1

        _at(reverse(p1.static))
      }
    }
  }
}
