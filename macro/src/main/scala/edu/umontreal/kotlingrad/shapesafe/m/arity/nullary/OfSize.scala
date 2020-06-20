package edu.umontreal.kotlingrad.shapesafe.m.arity.nullary

import edu.umontreal.kotlingrad.shapesafe.m.arity.Arity.FromOp
import edu.umontreal.kotlingrad.shapesafe.m.arity.Utils.NatAsOp
import edu.umontreal.kotlingrad.shapesafe.m.arity.Operand
import shapeless.ops.hlist
import shapeless.{HList, Nat}

class OfSize[-Data <: HList, S <: NatAsOp[_]](
    val number: Int
) extends Operand.ProvenToBe[FromOp[S]]()(new FromOp[S]()) {}

object OfSize {

  implicit def observe[Data <: HList, N <: Nat](
      implicit
      getSize: hlist.Length.Aux[Data, N],
      simplify: NatAsOp[N]
  ): OfSize[Data, NatAsOp[N]] = {

    new OfSize(simplify.value.asInstanceOf[Int])
  }

  //  implicit def fromNat[N <: Nat](n: N)(
  //      implicit simplify: NatAsOp[N]
  //  ): OfSize[Nothing, NatAsOp[N]] = {
  //    new OfSize(simplify.value)
  //  }

  def observe[Data <: HList, N <: Nat](v: Data)(
      implicit self: OfSize[Data, NatAsOp[N]]
  ): OfSize[Data, NatAsOp[N]] = {
    self
  }
}
