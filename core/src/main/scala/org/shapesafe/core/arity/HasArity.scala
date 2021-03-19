package org.shapesafe.core.arity

trait HasArity {

  type Internal <: Arity
  def internal: Internal
}
