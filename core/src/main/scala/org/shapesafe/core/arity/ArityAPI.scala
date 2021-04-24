package org.shapesafe.core.arity

import org.shapesafe.core.arity.ArityReporters.{InterruptArity, PeekArity}
import org.shapesafe.core.arity.ProveArity.|-
import org.shapesafe.core.arity.ops.ArityOpsLike
import org.shapesafe.core.axis.{Axis, NoName, NoNameW}
import org.shapesafe.core.debugging.OpStr.OpStr
import shapeless.Witness
import shapeless.Witness.Aux

import scala.language.implicitConversions

trait ArityAPI extends ArityOpsLike with Axis {

  override type _Axis >: this.type <: ArityAPI

  final override val nameSingleton: Aux[NoName] = NoNameW

  final override def toString: String = arity.toString

  def verify[
      O <: Arity.Verifiable
  ](
      implicit
      prove: _Arity |- O
  ): ArityAPI.^[O] = prove.apply(arity).value.^

  def eval[
      O <: LeafArity
  ](
      implicit
      prove: _Arity |- O
  ): ArityAPI.^[O] = verify(prove)

  def peek[
      O <: LeafArity
  ](
      implicit
      reporter: ArityReporters.PeekArity.Case[_Arity],
      prove: _Arity |- O
  ): ArityAPI.^[O] = verify(prove)

  def peekOnly(
      implicit
      reporter: PeekArity.Case[_Arity]
  ): this.type = this

  def interrupt(
      implicit
      reporter: InterruptArity.Case[_Arity]
  ): this.type = this
}

object ArityAPI {

  type Aux[A <: Arity] = ArityAPI { type _Arity = A }

  final case class ^[A <: Arity](arity: A) extends ArityAPI {

    override type _Arity = A

    type _Axis = ^[A]

    type _OpStr = OpStr[A]
    type _Expr = A#_Expr
  }

  implicit def unbox[A <: Arity](v: Aux[A]): A = v.arity
//  implicit def unbox[T <: ArityAPI](v: T): v._Arity = v._arity // TODO: why is it not effective?
//  implicit def box[T <: Arity](v: T): ^[T] = ArityAPI.^(v) // TODO: remove, type parameter is arbitrary

  implicit def fromIntS[T <: Int with Singleton](v: T)(
      implicit
      toW: Witness.Aux[T]
  ): ArityAPI.^[LeafArity.Literal[T]] = {

    Arity(toW)
  }
}
