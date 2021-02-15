package org.shapesafe.core.arity.binary

import org.shapesafe.core.arity.{Arity, Operator}
import shapeless.Poly2

trait Op2Like extends Operator {

  type On[
      A1 <: Arity,
      A2 <: Arity
  ]

  def apply[
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
        Op2Like.this.apply(a1, a2)
      }
    }
  }
}
