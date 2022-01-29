package shapesafe.core.arity.binary

import ai.acyclic.graph.commons.HasOuter
import shapesafe.core.arity.{Arity, ArityConjecture, ArityType}
import shapesafe.core.debugging.HasDebugSymbol
import singleton.ops.+

trait Op2Like extends Op2Like.DebuggingSupport {

  trait Conjecture2[
      A1 <: ArityType,
      A2 <: ArityType
  ] extends ArityConjecture
      with HasOuter {

    def a1: A1
    def a2: A2

    final def outer: Op2Like.this.type = Op2Like.this

    final override type _DebugSymbol = A1#_DebugSymbol + Debug[Unit, Unit]#_DebugSymbol + A2#_DebugSymbol
    // TODO: add Bracket
    final override type Expr = Debug[A1#Expr, A2#Expr]
  }

  type On[
      A1 <: ArityType,
      A2 <: ArityType
  ] <: Conjecture2[A1, A2]

  def on(
      a1: Arity,
      a2: Arity
  ): On[a1._ArityType, a2._ArityType]
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

object Op2Like extends Op2Like_Imp0 {

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
