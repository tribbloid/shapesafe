package org.shapesafe.core.arity.binary

import org.shapesafe.core.arity.{Arity, Operator}
import shapeless.{Poly2, Witness}

trait Op2Like extends Operator {

  import singleton.ops._

  type On[
      A1 <: Arity,
      A2 <: Arity
  ]

  def on[
      A1 <: Arity,
      A2 <: Arity
  ](
      a1: A1,
      a2: A2
  ): On[A1, A2]

  object AsShapelessPoly2 extends Poly2 {

    implicit def trivial[
        A1 <: Arity,
        A2 <: Arity
    ]: Case.Aux[A1, A2, On[A1, A2]] = {
      at[A1, A2].apply { (a1, a2) =>
        Op2Like.this.on(a1, a2)
      }
    }
  }
  type AsShapelessPoly2 = AsShapelessPoly2.type

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
