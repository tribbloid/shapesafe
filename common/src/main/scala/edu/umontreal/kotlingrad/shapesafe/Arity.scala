package edu.umontreal.kotlingrad.shapesafe

import shapeless.ops.hlist
import shapeless.ops.nat.ToInt
import shapeless.{HList, Nat}
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

  trait OfSizeLike[N <: Nat] extends Constant[N]

  case class OfSize[Data <: HList, N <: Nat](number: Int) extends OfSizeLike[N] {}

  object OfSize {

    implicit def observe[Data <: HList, T <: Nat](
        implicit
        getSize: hlist.Length.Aux[Data, T],
        toInt: ToInt[T]
    ): OfSize[Data, T] =
      new OfSize[Data, T](Nat.toInt[T])
  }

  trait OfIntLike extends Arity

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

  case class OfInt_Unsafe(number: Int) extends OfIntLike

  case class OfInt[Lit](w: Lt[Int]) extends Constant[Lit] with OfIntLike {
    override def number: Int = w.value
  }
  object OfInt {

    def safe(w: Lt[Int]): OfInt[w.T] = OfInt[w.T](w)
  }
}
