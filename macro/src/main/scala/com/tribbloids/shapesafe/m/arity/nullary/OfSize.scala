package com.tribbloids.shapesafe.m.arity.nullary

import com.tribbloids.shapesafe.m.arity.Arity.FromOp
import com.tribbloids.shapesafe.m.arity.Proven.ProvenToBe
import com.tribbloids.shapesafe.m.arity.Utils.NatAsOp
import shapeless.ops.hlist
import shapeless.{HList, Nat}

class OfSize[-Data <: HList, S <: NatAsOp[_]](
    val singleton: S
) extends ProvenToBe[FromOp[S]]()(new FromOp(singleton)) {}

object OfSize {

  implicit def observe[Data <: HList, N <: Nat](
      implicit
      getSize: hlist.Length.Aux[Data, N],
      simplify: NatAsOp[N]
  ): OfSize[Data, NatAsOp[N]] = {

    new OfSize(simplify)
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
