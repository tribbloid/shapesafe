package edu.umontreal.kotlingrad.spike.arity

import shapeless.Witness
import singleton.ops.+
import singleton.ops.impl.Op

package object attempt1 {

  trait Operand {

    def +[
        X >: this.type <: Operand,
        Y <: Operand
    ](that: Y): Op2[X, Y] = {

      Op2[X, Y](this, that)
    }
  }

  trait Arity extends Operand {}

  object Arity {

    trait Const[S] extends Arity {

      type SS = S
    }

    object Const {

      class Same[S] extends Proof.Invar[Const[S], S] {
        override def apply(v1: Const[S]): Const[S] = v1
      }

      implicit def summon[S]: Same[S] = new Same[S]
    }

    class FromOp[S <: Op]() extends Const[S]

    object FromOp {

      implicit def summon[S <: Op](implicit s: S): FromOp[S] = new FromOp[S]()
    }

    class FromLiteral[S <: Int](val w: Witness.Lt[Int]) extends Const[S] {}

    object FromLiteral {

      implicit def summon[S <: Int](implicit w: Witness.Aux[S]): FromLiteral[S] =
        new FromLiteral[S](w)
    }

    def apply(w: Witness.Lt[Int]): FromLiteral[w.T] = {

      FromLiteral.summon[w.T](w) //TODO: IDEA inspection error
    }

  }

  trait Proof[-I, +O] extends (I => O) with Serializable {}

  object Proof {

    trait Invar[-I, S] extends Proof[I, Arity.Const[S]] {

      type SS = S
    }
  }

  case class Op2[
      +A1 <: Operand,
      +A2 <: Operand
  ](
      a1: A1,
      a2: A2
  ) extends Operand {}

  object Op2 {

    class ProveInvar[
        A1 <: Operand,
        A2 <: Operand,
        S1,
        S2
    ](
        implicit
        bound1: Proof.Invar[A1, S1],
        bound2: Proof.Invar[A2, S2],
        lemma: S1 + S2
    ) extends Proof.Invar[Op2[A1, A2], S1 + S2] {
      override def apply(v1: Op2[A1, A2]): Arity.Const[S1 + S2] =
        Arity.FromOp.summon[S1 + S2](lemma)
    }

    implicit def summon[
        A1 <: Operand,
        A2 <: Operand,
        S1,
        S2
    ](
        implicit
        bound1: Proof.Invar[A1, S1],
        bound2: Proof.Invar[A2, S2],
        lemma: S1 + S2
    ): ProveInvar[A1, A2, S1, S2] = new ProveInvar[A1, A2, S1, S2]
  }

  def prove[T <: Operand, R <: Arity](a1: T)(implicit proof: Proof[T, R]): R = {
    proof.apply(a1)
  }

  def proveGeneric[T <: Operand](a1: T)(implicit proof: Proof[T, Arity]): Arity = {
    proof.apply(a1)
  }

  implicit val a = Arity(3)
  implicit val b = Arity(4)

  val p1 = {

    prove(a)

    proveGeneric(b)
  }

  val op = a + b

  val p2 = {

    // TODO: these doesn't work! why?
//    prove(op)
//
//    proveGeneric(op)
  }
//  summoner.summon(op) // oops
}
