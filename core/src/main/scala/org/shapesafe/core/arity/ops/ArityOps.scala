package org.shapesafe.core.arity.ops

import org.shapesafe.core.arity.Arity

case class ArityOps[A <: Arity](self: A) extends ArityOpsLike[A] {}

object ArityOps extends ArityOpsLike[Nothing] {

  override def self: Nothing = ???
}
