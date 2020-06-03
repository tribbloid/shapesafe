package edu.umontreal.kotlingrad.shapesafe

import shapeless.ops.hlist
import shapeless.ops.nat.ToInt
import shapeless.{HList, Nat}
import singleton.ops.{==, Require}

trait Arity extends Product with Serializable {}

object Arity {

  case object ?? extends Arity

  trait Fixed[N] extends Arity {
    type Number = N
    def number: Int

    def proveEqual_internal[N2](implicit self: Require[Number == N2]): Unit = {}
  }

  trait FromNat[N <: Nat] extends Fixed[N]

  case class OfSize[Data <: HList, N <: Nat](number: Int) extends FromNat[N] {}

  object OfSize {

    implicit def observe[Data <: HList, T <: Nat](
        implicit
        getSize: hlist.Length.Aux[Data, T],
        toInt: ToInt[T]
    ): OfSize[Data, T] = OfSize[Data, T](Nat.toInt[T])
  }

  case class FromLiteral[Lit](w: W.Lt[Int]) extends Fixed[Lit] {
    override def number: Int = w.value
  }

  object FromLiteral {

    def make(w: W.Lt[Int]): FromLiteral[w.T] = FromLiteral[w.T](w)

    // TODO: remove, Witness already has implicit conversion
    def makeWImplicit(number: Int)(
        implicit w: W.Lt[Int] = W(number)
    ): FromLiteral[w.T] = {

      make(w)
    }
  }
}
