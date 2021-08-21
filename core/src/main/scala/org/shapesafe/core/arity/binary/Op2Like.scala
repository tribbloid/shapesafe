package org.shapesafe.core.arity.binary

import org.shapesafe.graph.commons.util.HasOuter
import org.shapesafe.core.arity.{Arity, ArityAPI, ArityConjecture}
import org.shapesafe.core.debugging.Expressions.Expr
import org.shapesafe.core.debugging.HasDebugSymbol
import singleton.ops.+

trait Op2Like extends Op2Like.DebuggingSupport {

  trait Conjecture2[
      A1 <: Arity,
      A2 <: Arity
  ] extends ArityConjecture
      with HasOuter {

    def a1: A1
    def a2: A2

    final def outer: Op2Like.this.type = Op2Like.this

    final override type _DebugSymbol = A1#_DebugSymbol + Debug[Unit, Unit]#_DebugSymbol + A2#_DebugSymbol
    // TODO: add Bracket
    final override type _AsExpr = Debug[Expr[A1], Expr[A2]]
  }

  type On[
      A1 <: Arity,
      A2 <: Arity
  ] <: Conjecture2[A1, A2]

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

  trait DebuggingSupport {
    self: Op2Like =>

    type Debug[A1, A2] <: HasDebugSymbol
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
