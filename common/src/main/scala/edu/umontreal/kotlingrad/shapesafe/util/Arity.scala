package edu.umontreal.kotlingrad.shapesafe.util

import shapeless.{Nat, Witness}
import singleton.ops.{==, Require}

import scala.language.implicitConversions

trait Arity extends Serializable {

  def number: Int // run-time

  override def toString: String = {

    this.getClass.getSimpleName + ": " + number
  }
}

object Arity {

  import shapeless.Witness._

  trait Constant[N] extends Arity {

    type Number = N // compile-type

    @transient protected[shapesafe] object internal {

      def proveSame[N2](implicit self: N =:= N2): Unit = {}

      // 'prove' means happening only in compile-time
      def proveEqual[N2](implicit self: Require[Number == N2]): Unit = {

        //TODO: need run-time proof
      }

      def requireEqual(w: Witness.Lt[Int])(implicit self: Require[Number == w.T]): Unit = {

        proveEqual[w.T]

        require(w.value == number)
      }
    }
  }

  trait Unsafe extends Arity

  // Constant & Unsafe are mutually exclusive

  trait FromSizeLike

  case class FromSize[N](number: Int) extends Constant[N] with FromSizeLike {}

  trait FromNumber extends Arity

  case class FromInt_Unsafe(number: Int) extends Unsafe with FromNumber

  case class FromInt[Lit](number: Int) extends Constant[Lit] with FromNumber
  object FromInt {

    def safe(w: Lt[Int]): FromInt[w.T] = FromInt[w.T](w.value)
  }

  // TODO: these doesn't work
  //  see https://stackoverflow.com/questions/62205940/when-calling-a-scala-function-with-compile-time-macro-how-to-failover-smoothly
  //  trait OfIntLike_Implicit0 { // of lower priority in resolving
  //
  //    implicit def unsafe(number: Int): OfInt_Unsafe = OfInt_Unsafe(number)
  //  }
  //
  //  object OfIntLike extends OfIntLike_Implicit0 {
  //
  //    implicit def safe(w: Lt[Int]): OfInt[w.T] = OfInt.safe(w)
  //  }
}
