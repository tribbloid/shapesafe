package edu.umontreal.kotlingrad.shapesafe

import shapeless.ops.hlist
import shapeless.ops.nat.ToInt
import shapeless.{HList, Nat}

trait Arity extends Product with Serializable {}

object Arity {

  case object ?? extends Arity

  trait Fixed[T] extends Arity {

    def value: Int

    type _T = T
  }

  trait FromNat[T <: Nat] extends Fixed[T]

  case class OfSize[Data <: HList, T <: Nat](value: Int) extends FromNat[T] {}

  object OfSize {

    implicit def observe[Data <: HList, T <: Nat](
        implicit
        getSize: hlist.Length.Aux[Data, T],
        toInt: ToInt[T]
    ): OfSize[Data, T] = OfSize[Data, T](Nat.toInt[T])
  }

  trait FromLiteral[Lit] extends Fixed[Lit]

  object FromLiteral {}

}
