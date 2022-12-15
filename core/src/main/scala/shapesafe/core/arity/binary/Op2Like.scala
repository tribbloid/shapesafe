package shapesafe.core.arity.binary

import ai.acyclic.prover.commons.HasOuter
import shapesafe.core.arity.{Arity, ArityConjecture, ArityType}
import shapesafe.core.debugging.HasSymbolLit
import singleton.ops.+

trait Op2Like {

  type _NotationProto[_, _] <: HasSymbolLit

  trait Conjecture2[
      A1 <: ArityType,
      A2 <: ArityType
  ] extends ArityConjecture
      with HasOuter {

    def a1: A1
    def a2: A2

    final def outer: Op2Like.this.type = Op2Like.this

    final override type _SymbolLit = A1#SymbolTxt + _NotationProto[Unit, Unit]#SymbolTxt + A2#SymbolTxt
    final override type Notation = _NotationProto[A1#Notation, A2#Notation]
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

object Op2Like extends Op2Like_Imp0 {}
