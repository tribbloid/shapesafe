package shapesafe.core.shape

import ai.acyclic.prover.commons.refl.XString
import shapesafe.core.arity.Utils.NatAsOp
import shapesafe.core.debugging.HasNotation
import ai.acyclic.prover.commons.same.Same
import shapeless.Nat

trait Index extends Same.ByEquality.IWrapper with HasNotation {

  def value: Any
  final override protected def samenessDelegatedTo = value

  override lazy val toString: String = s"${value}:${getClass.getSimpleName}"
}

object Index {

  trait Name_<:[+KUB] extends Index {}
  type Str = Name_<:[String]

  class Name[S <: XString](val name: S) extends Name_<:[S] {
    type Name = S

    override def value = name

    override type Notation = S
  }

  object Name {

    def apply[S <: XString](w: S): Name[S] = new Name(w)
  }

  class LtoR[N <: Nat, S <: Int](val index: N, override val value: S) extends Name_<:[Nothing] {
    type Ordinal = N

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
