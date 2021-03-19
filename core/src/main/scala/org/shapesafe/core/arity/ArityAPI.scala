package org.shapesafe.core.arity

import org.shapesafe.core.arity.ProveArity.|-
import org.shapesafe.core.arity.ops.ArityOpsLike
import org.shapesafe.core.axis.{Axis, NoName, NoNameW}
import shapeless.Witness.Aux

import scala.language.implicitConversions

trait ArityAPI extends ArityOpsLike with Axis {

  final override type Name = NoName
  final override def nameSingleton: Aux[NoName] = NoNameW

  final lazy val runtimeTry = internal.runtimeTry
  final def runtimeArity: Int = runtimeTry.get
  final def runtimeOpt: Option[Int] = runtimeTry.toOption

  final override def toString: String = internal.fullStr

  final def verify[
      O <: Arity
  ](
      implicit
      prove: Internal |- O
  ): ArityAPI.^[O] = prove.apply(internal).value.^

  final def eval[
      O <: LeafArity
  ](
      implicit
      prove: Internal |- O
  ): ArityAPI.^[O] = prove.apply(internal).value.^

}

object ArityAPI {

  final case class ^[C <: Arity](internal: C) extends ArityAPI {

    override type Internal = C

    def ^ : ^[C] = this
  }

  implicit def unbox[T <: ArityAPI](v: T): v.Internal = v.internal

}
