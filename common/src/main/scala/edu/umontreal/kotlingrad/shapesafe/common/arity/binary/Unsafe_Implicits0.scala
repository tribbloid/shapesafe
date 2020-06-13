package edu.umontreal.kotlingrad.shapesafe.common.arity.binary

import edu.umontreal.kotlingrad.shapesafe.common.arity.ArityLike

trait Unsafe_Implicits0 {

  class _0[A1 <: ArityLike.Unsafe, A2 <: ArityLike](val a2: A2) extends Unsafe[A1, A2] {

    override object ProbablyEqual extends (A1 MayEqual A2) {

      override type Out = a2.Out
      override val Out: Out = a2.Out
    }
  } // of lower priority

  implicit def summon0[A1 <: ArityLike.Unsafe, A2 <: ArityLike](implicit a2: A2): _0[A1, A2] =
    new _0[A1, A2](a2)
}
