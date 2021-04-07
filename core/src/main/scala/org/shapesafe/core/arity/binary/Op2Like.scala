package org.shapesafe.core.arity.binary

import org.shapesafe.core.arity.{Arity, ArityAPI, ProveArity}
import org.shapesafe.core.debugging.InfoCT

trait Op2Like_Imp0 {

//  implicit def lastResort[
//      A <: Arity
//  ](
//      implicit
//      ev: GetInfoOf.Type.From[A]
//  ): A =>> Arity.LastResortInfo[A] = ProveArity.forAll[A].=>> { v =>
//    Arity.LastResortInfo(v)
//  }
}

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

    type On[
        A1 <: Arity,
        A2 <: Arity
    ] <: Arity

    def on(
        a1: ArityAPI,
        a2: ArityAPI
    ): On[a1._Arity, a2._Arity]
  }

  trait DebuggingSupport extends Base {

    import ProveArity.Factory._

    type CannotI[I1 <: InfoCT, I2 <: InfoCT]
    final type FailOn[I1 <: InfoCT, I2 <: InfoCT] = InfoCT.Fail[CannotI[I1, I2]]

    implicit def debug[
        A1 <: Arity,
        A2 <: Arity,
        I1 <: Arity.HasInfo,
        I2 <: Arity.HasInfo
    ](
        implicit
        toInfo1: ProveArity.|-[A1, I1],
        toInfo2: ProveArity.|-[A2, I2],
        fail: FailOn[I1, I2]
    ): On[A1, A2] =>> Arity = ProveArity.forAll[On[A1, A2]].=>> {
      ???
    }
  }
}
