package org.shapesafe.core.shape

import org.shapesafe.core.arity.Utils.NatAsOp
import org.shapesafe.core.debugging.CanPeek
import org.shapesafe.graph.commons.util.IDMixin
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

  class I_th[N <: Nat, S <: Int](val index: N, val value: S) extends Name_<:[Nothing] {
    type Ordinal = N

    override protected def _id = value

    override type Expr = S
  }

  object I_th {

//    import shapeless.ops.nat.ToInt

    def apply(i: Nat)(
        implicit
        asOp: NatAsOp[i.N]
    ) = new I_th[i.N, asOp.OutInt](i.asInstanceOf[i.N], asOp.value.asInstanceOf[asOp.OutInt])
  }
}
