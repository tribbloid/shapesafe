package org.shapesafe.core.arity.binary

import com.tribbloids.graph.commons.util.HasOuter
import org.shapesafe.core.arity.{Arity, ArityAPI}
import org.shapesafe.core.debugging.InfoCT
import org.shapesafe.core.debugging.InfoCT.{CanPeek, CanRefute}

trait Op2Like_Imp0 {}

trait Op2Like extends Op2Like.DebuggingSupport with Op2Like_Imp0 {

//  object AsShapelessPoly2 extends Poly2 {
//
//    implicit def trivial[
//        A1 <: ArityCore,
//        A2 <: ArityCore
//    ]: Case.Aux[A1, A2, On[A1, A2]] = {
//      at[A1, A2].apply { (a1, a2) =>
//        Op2Like.this.on(a1, a2)
//      }
//    }
//  }
//  type AsShapelessPoly2 = AsShapelessPoly2.type

}

object Op2Like {

  trait Base {
    self: Op2Like =>

    trait Conjecture2[
        A1 <: Arity,
        A2 <: Arity
    ] extends Arity
        with CanRefute
        with HasOuter {

      final def outer: Op2Like.this.type = Op2Like.this
    }

    type On[
        A1 <: Arity,
        A2 <: Arity
    ] <: Conjecture2[A1, A2]

    def on(
        a1: ArityAPI,
        a2: ArityAPI
    ): On[a1._Arity, a2._Arity]
  }

  trait DebuggingSupport extends Base {
    self: Op2Like =>

    type Refute[I1 <: CanPeek, I2 <: CanPeek] // TODO: remove this and use CanRefute
    final type RefuteFor[I1 <: CanPeek, I2 <: CanPeek] = InfoCT.ReportMsg[Refute[I1, I2]]

//    implicit def debug[
//        A1 <: Arity,
//        A2 <: Arity,
//        I1 <: Arity.HasInfo,
//        I2 <: Arity.HasInfo
//    ](
//        implicit
//        toInfo1: ProveArity.|-[A1, I1],
//        toInfo2: ProveArity.|-[A2, I2],
//        fail: FailOn[I1, I2]
//    ): On[A1, A2] =>> Arity = ProveArity.forAll[On[A1, A2]].=>> {
//      ???
//    }
  }
}
