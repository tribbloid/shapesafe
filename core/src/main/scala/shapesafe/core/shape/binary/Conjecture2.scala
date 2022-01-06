package shapesafe.core.shape.binary

import shapesafe.core.shape.binary.Conjecture2.Lt
import shapesafe.core.shape.{ProveShape, Shape, ShapeConjecture, Unchecked}

trait Conjecture2 extends ShapeConjecture {

  type SS1 <: Shape
  type SS2 <: Shape
}

trait Conjecture2_Imp0 {

  import ProveShape._

  implicit def leftUnchecked: Lt[Unchecked, _ <: Shape] |- Unchecked.type = {
    ProveShape.forAll.=>> { _ =>
      Unchecked
    }
  }

}

object Conjecture2 extends Conjecture2_Imp0 {

  type Lt[S1 <: Shape, S2 <: Shape] = Conjecture2 {

    type SS1 <: S1
    type SS2 <: S2
  }

  trait ^[S1 <: Shape, S2 <: Shape] extends Conjecture2 {

    final override type SS1 = S1
    final override type SS2 = S2
  }

  import ProveShape._

  implicit def rightUnchecked: Lt[_ <: Shape, Unchecked] |- Unchecked.type = {
    ProveShape.forAll.=>> { _ =>
      Unchecked
    }
  }

  //  object Refute2 extends ShapeReporters.RefuteReporter[Conjecture2 with CanRefute] {
//
//    override object Step1 extends Poly1Base[Iub, MsgBroker] {
//
//      implicit def evalS2[
//          SELF <: Iub,
//          M1 <: MsgBroker,
//          M2 <: MsgBroker
//      ](
//          implicit
//          forS1: ShapeReporters.PeekShape.Step1.Case.Aux[SELF#SS1, M1],
//          forS2: ShapeReporters.PeekShape.Step1.Case.Aux[SELF#SS1, M2]
//      ): SELF ==> Aux[Refute2[SELF, M1#Out, M2#Out]] = {
//
//        forAll[SELF].==> { _ =>
//          MsgBroker[Refute2[SELF, M1#Out, M2#Out]]
//        //          MsgBroker[M1#Out]
//        //          MsgBroker[Peek[SELF]]
//        //          MsgBroker[Peek[SELF#SS1]]
//        }
//      }
//    }
//  }

  // TODO: disabled for causing slow performance. Need a good bypass
//  implicit def refute2[
//      S1 <: Conjecture2
//  ](
//      implicit
//      step1: Refute2.Case[S1]
//  ): S1 |- S1 = {
//    ???
//  }
}
