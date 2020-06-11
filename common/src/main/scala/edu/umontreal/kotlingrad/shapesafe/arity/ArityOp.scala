package edu.umontreal.kotlingrad.shapesafe.arity

trait ArityOp {

  type Out <: Arity
}

object ArityOp {

//  trait UnsafeOp extends ArityOp {
//
//    type Out = Arity.Unsafe
//  }

  trait BinaryOp[-A1 <: Arity, -A2 <: Arity] extends ArityOp {

    // if singleton.ops TwoFace feature is fully utilised it probably doesn't need to be defined
    def out(a1: A1, a2: A2): Out
  }
}
