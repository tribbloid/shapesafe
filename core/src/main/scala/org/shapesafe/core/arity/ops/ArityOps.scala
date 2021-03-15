package org.shapesafe.core.arity.ops

import org.shapesafe.core.arity.{Arity, LeafArity}
import org.shapesafe.core.arity.ProveArity.|-

case class ArityOps[SELF <: Arity](self: SELF) extends ArityOpsLike[SELF] {

  final def verify[
      O <: Arity
  ](
      implicit
      prove: SELF |- O
  ): O = prove.apply(self).value

  final def eval[
      O <: LeafArity
  ](
      implicit
      prove: SELF |- O
  ): O = prove.apply(self).value
}

object ArityOps extends ArityOpsLike[Nothing] {

  override def self: Nothing = ???
}
