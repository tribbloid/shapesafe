package org.shapesafe.core.arity.binary

import com.tribbloids.graph.commons.util.HasOuter
import org.shapesafe.core.CompileTimeInfo.CanRefute
import org.shapesafe.core.arity.{Arity, ArityAPI, ArityConjecture}

trait Op2Like extends Op2Like.Op2Like_Imp0 {

  trait Conjecture2 extends ArityConjecture with HasOuter with CanRefute {

    final def outer: Op2Like.this.type = Op2Like.this

    //      final type _RefuteMsg = PeekInfo.Fail[_Refute]
  }

  type On[
      A1 <: Arity,
      A2 <: Arity
  ] <: Conjecture2 {}

  def on(
      a1: ArityAPI,
      a2: ArityAPI
  ): On[a1._Arity, a2._Arity]
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

  trait Op2Like_Imp0 {}
}
