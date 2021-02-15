package org.shapesafe.core.arity.nullary

import org.shapesafe.core.arity.LeafArity.Derived
import org.shapesafe.core.arity.Proven.ProvenAs
import org.shapesafe.core.arity.Utils.NatAsOp
import shapeless.ops.hlist
import shapeless.{HList, Nat}

// TODO: should not carry the proof
class OfSize[-DATA <: HList, S <: NatAsOp[_]](
    val singleton: S
) extends ProvenAs[Derived[S]]()(new Derived(singleton)) {

  override def runtime: Int = singleton.value.asInstanceOf[Int]
}

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
      implicit
      self: OfSize[Data, NatAsOp[N]]
  ): OfSize[Data, NatAsOp[N]] = {
    self
  }
}
