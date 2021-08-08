package org.shapesafe.core.shape.unary

import org.shapesafe.core.shape.{ProveShape, Shape, ShapeConjecture, UncheckedShape}

trait Conjecture1 extends ShapeConjecture {

  type SS1 <: Shape
}

object Conjecture1 {

  type Aux[S1 <: Shape] = Conjecture1 {
    type SS1 = S1
  }

  trait ^[S1 <: Shape] extends Conjecture1 {

    final type SS1 = S1
  }

  import ProveShape.ForAll._

  implicit def unchecked: Aux[UncheckedShape] =>> UncheckedShape.type = {
    ProveShape.forAll[Aux[UncheckedShape]].=>> { _ =>
      UncheckedShape
    }
  }

//  object Refute1 extends ShapeReporters.RefuteReporter[Conjecture1 with CanRefute] {
//
//    override object Step1 extends Poly1Base[Iub, MsgBroker] {
//
//      implicit def evalS1[
//          SELF <: Iub,
//          M1 <: MsgBroker
//      ](
//          implicit
//          forS1: ShapeReporters.PeekShape.Step1.Case.Aux[SELF#SS1, M1]
//      ): SELF ==> Aux[Refute1[SELF, M1#Out]] = {
//
//        forAll[SELF].==> { _ =>
//          MsgBroker[Refute1[SELF, M1#Out]]
////          MsgBroker[M1#Out]
////          MsgBroker[Peek[SELF]]
////          MsgBroker[Peek[SELF#SS1]]
//        }
//      }
//    }
//  }
//
//  implicit def refute1[
//      S1 <: Conjecture1 with CanRefute
//  ](
//      implicit
//      step1: Refute1.Case[S1]
//  ): S1 |- Shape = {
//    ???
//  }
}
