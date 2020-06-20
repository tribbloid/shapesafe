package edu.umontreal.kotlingrad.shapesafe.m.arity

import edu.umontreal.kotlingrad.shapesafe.m.arity.binary.Op2
import singleton.ops.{+, /}

// for "Expression"
trait Operand {

  def +(that: Operand): Op2[Operand.this.type, that.type, +] = {

    Op2[this.type, that.type, +](this, that, _ + _)
  }

  def /(that: Operand): Op2[Operand.this.type, that.type, /] = {

    Op2[this.type, that.type, /](this, that, _ / _)
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
