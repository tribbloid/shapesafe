package org.shapesafe.core.arity.binary

import com.tribbloids.graph.commons.util.HasOuter
import org.shapesafe.core.arity.{Arity, ArityAPI, ArityConjecture}
import org.shapesafe.core.debugging.Expr.Expr
import org.shapesafe.core.debugging.{Expr, OpStr}

trait Op2Like_Imp0 {}

trait Op2Like extends Op2Like.DebuggingSupport with Op2Like_Imp0 {

  type Symbol[A1, A2] <: Expr.HasLiteral

  trait Conjecture2[
      A1 <: Arity,
      A2 <: Arity
  ] extends ArityConjecture
      with HasOuter {

    final def outer: Op2Like.this.type = Op2Like.this

    final override type _OpStr = OpStr.Infix[A1, Symbol[Unit, Unit]#Lit, A2] // TODO: add Bracket
    override type _Expr = Symbol[Expr[A1], Expr[A2]]
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
