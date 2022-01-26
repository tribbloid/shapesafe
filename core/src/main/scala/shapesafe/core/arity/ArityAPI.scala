package shapesafe.core.arity

import shapeless.Witness.Aux
import shapesafe.core.arity.ArityReporters.{InterruptArity, PeekArity}
import shapesafe.core.arity.ProveArity.|-
import shapesafe.core.arity.ops.ArityOpsLike
import shapesafe.core.axis.{Axis, NoName, NoNameW}

import scala.language.implicitConversions

trait ArityAPI extends ArityOpsLike with Axis {

  final override def toString: String = arity.toString

  def verify[
      O <: Arity
  ](
      implicit
      prove: _Arity |- O
  ): ArityAPI.^[O] = prove.consequentFor(arity).value.^

  def eval[
      O <: LeafArity
  ](
      implicit
      prove: _Arity |- O
  ): ArityAPI.^[O] = verify(prove)

  def peek(
      implicit
      reporter: PeekArity.Case[_Arity]
  ): this.type = this

  def interrupt(
      implicit
      reporter: InterruptArity.Case[_Arity]
  ): this.type = this

  def reason[
      O <: LeafArity
  ](
      implicit
      reporter: ArityReporters.PeekArity.Case[_Arity],
      prove: _Arity |- O
  ): ArityAPI.^[O] = eval(prove)

  final override val nameW: Aux[NoName] = NoNameW
}

object ArityAPI {

  type Aux[A <: Arity] = ArityAPI { type _Arity = A }

  final case class ^[A <: Arity](arity: A) extends ArityAPI {

    override type _Arity = A

    type _Axis = ^[A]

    type Expr = A#Expr
  }

  implicit def unbox[A <: Arity](v: Aux[A]): A = v.arity
//  implicit def unbox[T <: ArityAPI](v: T): v._Arity = v._arity // TODO: why is it not effective?
}
