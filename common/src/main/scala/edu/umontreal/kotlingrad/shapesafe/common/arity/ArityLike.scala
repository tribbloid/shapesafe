package edu.umontreal.kotlingrad.shapesafe.common.arity

trait ArityLike extends Serializable {

  type Out <: Arity
  def Out: Out
}

object ArityLike {

  type Aux[T] = ArityLike { type Out = T }
  type Lt[T] = ArityLike { type Out <: T }

  type Unsafe = Lt[Arity.Unknown.type]

  type Const[N <: IntOp] = Lt[Arity.Constant[N]]
}
