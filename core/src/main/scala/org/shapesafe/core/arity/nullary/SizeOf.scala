package org.shapesafe.core.arity.nullary

import org.shapesafe.core.arity.ProveArity._
import org.shapesafe.core.arity.Utils.NatAsOp
import org.shapesafe.core.arity.{ConstArity, ProveArity, VerifiedArity}
import shapeless.ops.hlist
import shapeless.{HList, Nat}

// TODO: should not carry the proof
case class SizeOf[D <: HList](data: D) extends VerifiedArity {

  override def runtimeValue: Int = data.runtimeLength
}

object SizeOf {

  import ProveArity.ForAll._

  implicit def observe[D <: HList, N <: Nat](
      implicit
      length: hlist.Length.Aux[D, N],
      simplify: NatAsOp[N]
  ): SizeOf[D] =>> ConstArity.Derived[simplify.type, simplify.OutInt] = {

    ProveArity.forAll[SizeOf[D]].=>> { v =>
      ConstArity.Derived.summon(simplify)
    }
  }

  //  implicit def fromNat[N <: Nat](n: N)(
  //      implicit simplify: NatAsOp[N]
  //  ): OfSize[Nothing, NatAsOp[N]] = {
  //    new OfSize(simplify.value)
  //  }

  def getConst[
      D <: HList,
      N <: Nat,
      O <: ConstArity[_]
  ](data: D)(
      implicit
      self: SizeOf[D] |- O
  ): O = {
    val raw = SizeOf[D](data)
    self.apply(raw).value
  }
}
