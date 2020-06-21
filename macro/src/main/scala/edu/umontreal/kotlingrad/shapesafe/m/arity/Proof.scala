package edu.umontreal.kotlingrad.shapesafe.m.arity

/**
  * is a view class
  */
trait Proof extends Serializable {

  type In <: Operand
  val in: In

  type Out <: Arity
  val out: Out

  object Fn extends (In => Out) {
    override def apply(v1: In): Out = out
  }
}

object Proof {

  type Aux[T] = Proof { type Out = T }
  type Lt[T] = Proof { type Out <: T }

  trait Unsafe extends Proof {

    final type Out = Arity.Unknown.type
    final val out: Out = Arity.Unknown
  }

  // TODO: type Out should be a parameter instead of a dependent type
  //  otherwise inferring Out will require Lazy
  trait Invar[S] extends Proof {
    type Out <: Arity.Const[S]

    type SS = S
  } // can't use type alias? really?

  def convert[In <: Operand, Out <: Arity](in: In)(
      implicit
      prove: In => Proof.Aux[Out]
  ): Out = {
    prove(in).out
  }
}
