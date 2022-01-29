package shapesafe.core.shape

import shapesafe.core.arity.Utils.NatAsOp
import shapesafe.core.debugging.CanPeek
import ai.acyclic.graph.commons.IDMixin
import shapeless.{Nat, Witness}

trait Index extends IDMixin with CanPeek {

  override lazy val toString: String = s"${_id}:${getClass.getSimpleName}"
}

object Index {

  trait Name_<:[+KUB] extends Index {}
  type Str = Name_<:[String]

  class Name[S <: String](val w: Witness.Aux[S]) extends Name_<:[S] {
    def name: S = w.value
    type Name = S

    override protected def _id = w.value

    override type Expr = S
  }

  object Name {

    def apply(w: Witness.Lt[String]): Name[w.T] = new Name(w)
  }

  class Left[N <: Nat, S <: Int](val index: N, val value: S) extends Name_<:[Nothing] {
    type Ordinal = N

    override protected def _id = value

    override type Expr = S
  }

  object Left {

//    import shapeless.ops.nat.ToInt

    def apply(i: Nat)(
        implicit
        asOp: NatAsOp[i.N]
    ) = new Left[i.N, asOp.OutInt](i.asInstanceOf[i.N], asOp.value.asInstanceOf[asOp.OutInt])
  }
}
