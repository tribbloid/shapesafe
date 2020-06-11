package edu.umontreal.kotlingrad.shapesafe.arity.proof

import edu.umontreal.kotlingrad.shapesafe.arity.{Arity, ArityOp}
import shapeless.ops.hlist
import shapeless.ops.nat.ToInt
import shapeless.{HList, Nat}

case class OfSize[Data <: HList, N <: Nat](number: Int) extends ArityOp {

  type _N = singleton.ops.ToInt[N] // getting rid of church encoding

  type Out = Arity.FromSize[_N]
  lazy val out: Out = Arity.FromSize[_N](number)
}

object OfSize {

  implicit def observe[Data <: HList, T <: Nat](
      implicit
      getSize: hlist.Length.Aux[Data, T],
      toInt: ToInt[T]
  ): OfSize[Data, T] = {

    new OfSize[Data, T](Nat.toInt[T])
  }
}
