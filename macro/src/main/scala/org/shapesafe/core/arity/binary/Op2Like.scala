package org.shapesafe.core.arity.binary

import org.shapesafe.core.arity.{Arity, Operator}

trait Op2Like extends Operator {

  def apply[
      A1 <: Arity,
      A2 <: Arity
  ](
      a1: A1,
      a2: A2
  ): Arity

}
