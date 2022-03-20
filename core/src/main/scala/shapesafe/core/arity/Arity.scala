package shapesafe.core.arity

import shapeless.{Nat, Witness}
import shapesafe.core.arity.ArityReporters.{InterruptArity, PeekArity}
import shapesafe.core.arity.ConstArity.{Derived, Literal}
import shapesafe.core.arity.ProveArity.|-
import shapesafe.core.arity.Utils.NatAsOp
import shapesafe.core.arity.ops.ArityOpsLike
import shapesafe.core.axis.Axis
import shapesafe.core.logic.Evaluable
import shapesafe.core.{arity, Const}

import scala.language.implicitConversions

trait Arity extends Evaluable with ArityOpsLike with Axis {

  import Arity._

  override val theory: ProveArity.type = ProveArity
  type EvalTo = LeafArity

  final override def toString: String = arityType.toString

  // TODO: aggregate to Evaluable
  def verify[
      O <: ArityType
  ](
      implicit
      prove: _ArityType |- O
  ): ^[O] = prove.consequentFor(arityType).value.^

  def eval[
      O <: LeafArity
  ](
      implicit
      prove: _ArityType |- O
  ): ^[O] = verify(prove)

  def peek(
      implicit
      reporter: PeekArity.Case[_ArityType]
  ): this.type = this

  def interrupt(
      implicit
      reporter: InterruptArity.Case[_ArityType]
  ): this.type = this

  def reason[
      O <: LeafArity
  ](
      implicit
      reporter: ArityReporters.PeekArity.Case[_ArityType],
      prove: _ArityType |- O
  ): ^[O] = eval(prove)

  final override val nameW: Witness.Aux[Const.NoName] = Const.NoNameW
}

object Arity {

  type Aux[A <: ArityType] = Arity { type _ArityType = A }

  final case class ^[A <: ArityType](arityType: A) extends Arity {

    override type _ArityType = A

    override type _Axis = ^[A]

    override type Notation = A#Notation
  }

  implicit def unbox[A <: ArityType](v: Aux[A]): A = v.arityType
//  implicit def unbox[T <: ArityAPI](v: T): v._Arity = v._arity // TODO: why is it not effective?

  val Unprovable: ^[arity.Unprovable.type] = arity.Unprovable.^

  val Unchecked: ^[arity.Unchecked.type] = arity.Unchecked.^

  def apply(w: Witness.Lt[Int]): ^[Literal[w.T]] = {
    ^(Literal.apply(w))
  }

  lazy val _0 = Arity(0)

  lazy val _1 = Arity(1)

  lazy val _2 = Arity(2)

  lazy val _3 = Arity(3)

  object FromNat {

    def apply[N <: Nat](v: N)(
        implicit
        asOp: NatAsOp[N]
    ): ^[Derived[NatAsOp[N], asOp.OutInt]] = {

      ^(Derived.summon[NatAsOp[N]](asOp))
    }
  }
}
