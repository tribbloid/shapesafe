package org.shapesafe.core.arity.ops

import org.shapesafe.core.arity.{Arity, LeafArity}
import org.shapesafe.core.arity.ProveArity.|-

case class ArityOps[A <: Arity](self: A) extends ArityOpsLike[A] {

  final def verify[
      O <: Arity
  ](
      implicit
      prove: A |- O
  ): O = prove.apply(self).value

  final def eval[
      O <: LeafArity
  ](
      implicit
      prove: A |- O
  ): O = prove.apply(self).value
}

object ArityOps extends ArityOpsLike[Nothing] {

  override def self: Nothing = ???
}
