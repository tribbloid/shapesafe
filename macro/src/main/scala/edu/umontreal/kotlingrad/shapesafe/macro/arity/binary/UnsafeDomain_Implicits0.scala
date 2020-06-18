package edu.umontreal.kotlingrad.shapesafe.`macro`.arity.binary

import edu.umontreal.kotlingrad.shapesafe.`macro`.arity.Proof

trait UnsafeDomain_Implicits0 {

  class _0[A1 <: Proof.Unsafe, A2 <: Proof](val a2: A2) extends UnsafeDomain[A1, A2] {

    override object ProbablyEqual extends (A1 MayEqual A2) {

      override type Out = a2.Out
      override val out: Out = a2.out
    }
  } // of lower priority

  implicit def summon0[A1 <: Proof.Unsafe, A2 <: Proof](implicit a2: A2): _0[A1, A2] =
    new _0[A1, A2](a2)
}
