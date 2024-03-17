package shapesafe.core.arity

import ai.acyclic.prover.commons.refl.XInt
import ai.acyclic.prover.commons.same.Same
import shapesafe.core.arity.Utils.Op
import singleton.ops.{==, Require}

trait ConstArity[S] extends LeafArity with Same.ByEquality.IWrapper {

  type SS = S
  def singleton: S

  final override type Notation = S

  final override def samenessDelegatedTo: S = singleton

  def proveSameType[N2](
      implicit
      proof: S =:= N2
  ): Unit = {}

  def proveEqualType[N2 <: XInt](
      implicit
      proof: Require[S == N2]
  ): Unit = {}

  // TODO: should be named proofEqual, require should do everything in runtime?
  def proveEqual[N2 <: XInt](n2: N2)(
      implicit
      proof: Require[S == N2]
  ): Unit = {

    require(n2 == runtimeValue)
  }
}

object ConstArity {

  case class Derived[OP <: Op, OUT <: Int] private (override val singleton: OUT) extends ConstArity[OUT] {
    override lazy val runtimeValue: Int = singleton
  }

  object Derived {

    implicit def summon[S <: Op](
        implicit
        s: S
    ): Derived[S, s.OutInt] = {
      new Derived[S, s.OutInt](s.value.asInstanceOf[s.OutInt])
    }
  }

  // this makes it impossible to construct directly from Int type
  case class Literal[S <: XInt](val singleton: S) extends ConstArity[S] {

    override def runtimeValue: Int = singleton
  }

  object Literal {

    implicit def summon[S <: XInt](
        implicit
        w: ValueOf[S]
    ): Literal[S] = {
      new Literal[S](w.value)
    }

  }
}
