package edu.umontreal.kotlingrad.shapesafe.arity.proof

import edu.umontreal.kotlingrad.shapesafe.arity.{Arity, ArityOp}
import shapeless.ops.hlist
import shapeless.{HList, Nat}
import singleton.ops.ToInt

case class OfSize[Data <: HList, N](number: Int) extends ArityOp {

  type Out = Arity.FromSize[N]
  lazy val out: Out = Arity.FromSize[N](number)
}

object OfSize {

  implicit def observe[Data <: HList, T <: Nat](
      implicit
      getSize: hlist.Length.Aux[Data, T],
      toInt: ToInt[T]
  ): OfSize[Data, ToInt[T]] = {

    new OfSize[Data, ToInt[T]](toInt.value.asInstanceOf[Int])
  }
}
