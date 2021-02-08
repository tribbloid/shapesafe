package org.shapesafe.m.arity.nullary

import org.shapesafe.m.arity.Arity.Derived
import org.shapesafe.m.arity.ProvenExpression.ProvenToBe
import org.shapesafe.m.arity.Utils.NatAsOp
import shapeless.ops.hlist
import shapeless.{HList, Nat}

class OfSize[-DATA <: HList, S <: NatAsOp[_]](
    val singleton: S
) extends ProvenToBe[Derived[S]]()(new Derived(singleton)) {}

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
