package org.shapesafe.core.arity.ops

import org.shapesafe.core.arity.Arity

case class ArityOps[X <: Arity](self: X) extends ArityOpsLike[X] {}

object ArityOps extends ArityOpsLike[Nothing] {

  override def self: Nothing = ???
}
