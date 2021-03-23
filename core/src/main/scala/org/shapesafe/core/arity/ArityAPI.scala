package org.shapesafe.core.arity

import org.shapesafe.core.arity.ProveArity.|-
import org.shapesafe.core.arity.ops.ArityOpsLike
import org.shapesafe.core.axis.{Axis, NoName, NoNameW}
import shapeless.Witness
import shapeless.Witness.Aux

import scala.language.implicitConversions

trait ArityAPI extends ArityOpsLike with Axis {

  override type AxisSelf >: this.type <: ArityAPI

  final override val nameSingleton: Aux[NoName] = NoNameW

  final override def toString: String = arityInner.fullStr

  final def verify[
      O <: Arity
  ](
      implicit
      prove: ArityInner |- O
  ): ArityAPI.^[O] = prove.apply(arityInner).value.^

  final def eval[
      O <: LeafArity
  ](
      implicit
      prove: ArityInner |- O
  ): ArityAPI.^[O] = prove.apply(arityInner).value.^
}

object ArityAPI {

  final case class ^[A <: Arity](arityInner: A) extends ArityAPI {

    override type ArityInner = A

    type AxisSelf = ^[A]
  }

  object ^ {

    implicit def unbox[A <: Arity](v: ^[A]): A = v.arityInner
  }

  implicit def unbox[T <: ArityAPI](v: T): v.ArityInner = v.arityInner // TODO: why is it not effective?
  implicit def box[T <: Arity](v: T): ^[T] = ArityAPI.^(v)

  implicit def fromIntS[T <: Int with Singleton](v: T)(
      implicit
      toW: Witness.Aux[T]
  ): ArityAPI.^[LeafArity.Literal[T]] = {

    Arity(toW)
  }
}
