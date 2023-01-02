package shapesafe.core.arity

import shapeless.{Nat, Witness}
import shapesafe.core.arity.ConstArity.{Derived, Literal}
import shapesafe.core.arity.Utils.NatAsOp
import shapesafe.core.arity.ops.ArityOpsLike
import shapesafe.core.axis.Axis
import shapesafe.core.debugging.CanReason
import shapesafe.core.{arity, Const, XInt}

import scala.language.implicitConversions

trait Arity extends CanReason with ArityOpsLike with Axis {

  import Arity._

  override val theory: ProveArity.type = ProveArity

  override def expressionType: _ArityType = arityType

  import theory._
  import AsLeafArity._

  final override def toString: String = arityType.toString

  // TODO: aggregate to CanReason
  def verify[
      O <: ArityType
  ](
      implicit
      prove: _ArityType |- O
  ): ^[O] = prove.consequentFor(arityType).value.^

  type EvalTo[O <: LeafArity] = _ArityType |- O
  def eval[
      O <: LeafArity
  ](
      implicit
      prove: EvalTo[O]
  ): ^[O] = verify(prove)

  def peek(
      implicit
      reporter: Peek[_ArityType]
  ): this.type = this

  def interrupt(
      implicit
      reporter: Interrupt[_ArityType]
  ): this.type = this

  type ReasonTo[O <: LeafArity] = this._ArityType |-@- O
  def reason[
      O <: LeafArity
  ](
      implicit
      prove: ReasonTo[O]
  ): ^[O] = eval(prove)

  final override val nameW: Witness.Aux[Const.NoName] = Const.NoNameW
}

object Arity {

  type Aux[A <: ArityType] = Arity { type _ArityType = A }

  final case class ^[SELF <: ArityType](arityType: SELF) extends Arity {

    override type _ArityType = SELF

    override type _Axis = ^[SELF]

    override type Notation = SELF#Notation
  }

  implicit def unbox[A <: ArityType](v: Aux[A]): A = v.arityType
//  implicit def unbox[T <: ArityAPI](v: T): v._Arity = v._arity // TODO: why is it not effective?

  val Unprovable: ^[arity.Unprovable.type] = arity.Unprovable.^

  val Unchecked: ^[arity.Unchecked.type] = arity.Unchecked.^

  def apply(w: Witness.Lt[XInt]): ^[Literal[w.T]] = {
    ^(Literal.shapeless(w))
  }

  object FromNat {

    def apply[N <: Nat](v: N)(
        implicit
        asOp: NatAsOp[N]
    ): ^[Derived[NatAsOp[N], asOp.OutInt]] = {

      ^(Derived.summon[NatAsOp[N]](asOp))
    }
  }

  val _0 = Arity(0)
  val _1 = Arity(1)
  val _2 = Arity(2)
  val _3 = Arity(3)
}
