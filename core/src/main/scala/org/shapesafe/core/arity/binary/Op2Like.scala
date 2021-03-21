package org.shapesafe.core.arity.binary

import org.shapesafe.core.arity.{Arity, ArityAPI, Operator}
import shapeless.Witness

trait Op2Like extends Operator {

  import singleton.ops._

  type On[
      +A1 <: Arity,
      +A2 <: Arity
  ] <: Arity

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

  type MsgTitle = Op2Like.msgTitle.T
  type MsgInfix

  object Msg {

    final val LF = Witness("\n")

    type Infix[S1, S2] = S1 + MsgInfix + S2
  }
}

object Op2Like {

  val msgTitle = Witness("No can do:\n")
}
