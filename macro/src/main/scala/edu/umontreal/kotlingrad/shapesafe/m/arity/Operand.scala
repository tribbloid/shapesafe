package edu.umontreal.kotlingrad.shapesafe.m.arity

import edu.umontreal.kotlingrad.shapesafe.m.arity.binary.Op2
import singleton.ops.{+, /}

// for "Expression"
trait Operand {

  def +[A2 <: Operand](that: A2): Op2[Operand.this.type, A2, +] = {

    Op2[this.type, A2, +](this, that, _ + _)
  }

  def /[A2 <: Operand](that: A2): Op2[Operand.this.type, A2, /] = {

    Op2[this.type, A2, /](this, that, _ / _)
  }
}

object Operand {

  trait Proven extends Operand with Proof {

    final override def self: this.type = this
  }

  abstract class ProvenToBe[O <: Arity]()(implicit override val out: O) extends Proven {

    type Out = O
  }

//  object Proven {
//
//    // TODO: can this be simplified?
//    class ProofImpl[T <: Proven]() extends Proof[T] {
//
//      override type Out = T#Out
//
//      override def simplify(op: T): Out = op.out
//    }
//
//    implicit def prove[T <: Proven]: ProofImpl[T] = new ProofImpl[T]()
//  }

}
