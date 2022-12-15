package shapesafe.core.shape

import shapesafe.core.arity.Utils.NatAsOp
import shapesafe.core.debugging.CanPeek
import ai.acyclic.prover.commons.EqualBy
import shapeless.{Nat, Witness}

trait Index extends EqualBy with CanPeek {

  override lazy val toString: String = s"${_equalBy}:${getClass.getSimpleName}"
}

object Index {

  trait Name_<:[+KUB] extends Index {}
  type Str = Name_<:[String]

  class Name[S <: String](val w: Witness.Aux[S]) extends Name_<:[S] {
    def name: S = w.value
    type Name = S

    override protected def _equalBy = w.value

    override type Notation = S
  }

  object Name {

    def apply(w: Witness.Lt[String]): Name[w.T] = new Name(w)
  }

  class LtoR[N <: Nat, S <: Int](val index: N, val value: S) extends Name_<:[Nothing] {
    type Ordinal = N

    override protected def _equalBy = value

    override type Notation = S
  }

  object LtoR {

//    import shapeless.ops.nat.ToInt

    def apply(i: Nat)(
        implicit
        asOp: NatAsOp[i.N]
    ) = new LtoR[i.N, asOp.OutInt](i.asInstanceOf[i.N], asOp.value.asInstanceOf[asOp.OutInt])
  }
}
