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

  type Aux[O <: Arity] = Proof { type Out = O }
  type Lt[O <: Arity] = Proof { type Out <: O }

  trait From[I <: Operand] extends Proof {

    final type In = I
  }

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

  case class Require[P <: Proof]() {

    def convert[_In <: Operand](in: _In)(
        implicit
        prove: _In => P
    ) = {
      prove(in).out
    }
  }

}
