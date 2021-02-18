package org.shapesafe.core.shape.binary

import org.shapesafe.core.axis.Axis
import org.shapesafe.core.shape.LeafShape.><
import org.shapesafe.core.shape.ProveShape._
import org.shapesafe.core.shape.ops.LeafShapeOps
import org.shapesafe.core.shape.{LeafShape, Shape, VerifiedShape}
import shapeless.HList
import shapeless.ops.hlist.Prepend

case class Direct[
    S1 <: Shape,
    S2 <: Shape
](
    s1: S1,
    s2: S2
) extends VerifiedShape {}

trait Direct_Imp0 {

  implicit def fold[
      S1 <: Shape,
      P1 <: LeafShape,
      S2 <: Shape,
      P2 <: LeafShape,
      HO <: HList,
      O <: LeafShape
  ](
      implicit
      lemma1: S1 ~~> P1,
      lemma2: S2 ~~> P2,
      concat: Prepend.Aux[P2#Static, P1#Static, HO],
      toShape: LeafShape.FromStatic.==>[HO, O]
  ): Direct[S1, S2] =>> O = {

    from[Direct[S1, S2]].out { direct =>
      val p1: P1 = lemma1.valueOf(direct.s1)
      val p2: P2 = lemma2.valueOf(direct.s2)

      toShape(concat(p2.static, p1.static))
    }
  }
}

object Direct extends Direct_Imp0 {

  // shortcut for trivial P1 + 1 case
  implicit def append[
      S1 <: Shape,
      P1 <: LeafShape,
      S2 <: Shape,
      A2 <: Axis,
      P2 <: Shape.Vector[A2]
  ](
      implicit
      lemma1: S1 ~~> P1,
      lemma2: S2 ~~> P2
  ): Direct[S1, S2] =>> (P1 >< A2) = {

    from[Direct[S1, S2]].out { direct =>
      val p1: P1 = lemma1.valueOf(direct.s1)
      val p2: P2 = lemma2.valueOf(direct.s2)
      val a2: A2 = p2.head

      new LeafShapeOps[P1](p1) >|< a2
    }
  }

  //  implicit def noOp[
  //      P1 <: LeafShape
  //  ]: Direct[P1, LeafShape.Eye] =>> P1 = {
  //
  //    from[Direct[P1, LeafShape.Eye]].out { dd =>
  //      dd.s1
  //    }
  //  }
  //
  //  implicit def recursive[
  //      P1 <: LeafShape,
  //      HEAD2 <: Axis,
  //      TAIL2 <: LeafShape,
  //      O <: LeafShape
  //  ](
  //      next: Direct[P1 >< HEAD2, TAIL2] ~~> O
  //  ) = {
  //
  //    from[Direct[P1, TAIL2 >< HEAD2]].out { dd =>
  //      val plus1 = dd.s1 >< dd.s2.head
  //      val alt = Direct(plus1, dd.s2.tail)
  //      next()
  //    }
  //  }
}