package org.shapesafe.core.arity.ops

import org.shapesafe.core.arity.Arity

trait HasArity {

  type _Arity <: Arity
  def arity: _Arity
}
