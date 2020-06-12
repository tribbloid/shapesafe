package edu.umontreal.kotlingrad.spike.arity

import edu.umontreal.kotlingrad.spike.arity.Arity.FromLiteral
import shapeless.Witness
import singleton.ops.impl.Op
import singleton.ops.{==, Require}

import scala.language.implicitConversions

trait Arity {

  def numberOpt: Option[Int] // run-time

  lazy val valueStr: String = numberOpt.map(_.toString).getOrElse(s"(${this.getClass.getSimpleName})")

  override def toString: String = {

    this.getClass.getSimpleName + ": " + valueStr
  }
}

object Arity {

  import Witness._

  trait Leaf extends Arity {

    final val out = this
  }

  object Unknown extends Arity {

    override def numberOpt: Option[Int] = None
  }

  trait Constant[N] extends Arity {

    type Number = N // compile-type

    def number: Int

    final override def numberOpt: Option[Int] = Some(number)

    @transient protected[shapesafe] object internal {

      def proveSame[N2](implicit self: Number =:= N2): Unit = {}

      // 'prove' means happening only in compile-time
      def proveEqual[N2](implicit self: Require[Number == N2]): Unit = {

        //TODO: need run-time proof

      }

      def requireEqual(w: Lt[Int])(implicit self: Require[Number == w.T]): Unit = {

        proveEqual[w.T]

        require(w.value == number)
      }
    }
  }

  // DO NOT directly carry Nat type! (church encoding is slow) convert to Op type first
  class FromSize[N <: Int with Op](val number: N) extends Constant[N]

  object FromSize {

    implicit def summon[N <: Int with Op](implicit ev: N): FromSize[N] = new FromSize[N](ev)
  }

  class FromOp[N <: Int with Op](val number: N) extends Constant[N]

  object FromOp {

    implicit def summon[N <: Int with Op](implicit ev: N): FromOp[N] = new FromOp[N](ev)
  }

  // this makes it impossible to construct directly from Int type
  class FromLiteral[N <: Int](val witness: Witness.Aux[N]) extends Constant[N] {

    override def number: N = witness.value
  }

  object FromLiteral {

    implicit def summon[N <: Int](implicit ev: Witness.Aux[N]): FromLiteral[N] = new FromLiteral[N](ev)

    def create(w: Witness.Lt[Int]): FromLiteral[w.T] = {

      summon[w.T](w)
    }
  }
}

object ArityObj {

  implicitly[Witness.Aux[Witness.`3`.T]]

  val summoned: FromLiteral[Witness.`3`.T] = implicitly[FromLiteral[Witness.`3`.T]]

  val a1 = FromLiteral.create(3)
}
