package org.shapesafe.core.arity

import org.shapesafe.graph.commons.util.IDMixin
import org.shapesafe.core.arity.Utils.Op
import shapeless.Witness
import singleton.ops.{==, Require, ToString}

trait Const[S] extends LeafArity with IDMixin {

  type SS = S
  def singleton: S

  override type _AsOpStr = ToString[S]
  override type _AsExpr = S

  override lazy val _id: S = singleton

  def proveSameType[N2](
      implicit
      proof: S =:= N2
  ): Unit = {}

  def proveEqualType[N2](
      implicit
      proof: Require[S == N2]
  ): Unit = {}

  // TODO: should be named proofEqual, require should do everything in runtime?
  def requireEqual(w: Witness.Lt[Int])(
      implicit
      proof: Require[S == w.T]
  ): Unit = {

    proveEqualType[w.T]

    require(w.value == runtimeArity)
  }
}

object Const {

  import Witness._

  class Derived[OP <: Op, OUT <: Int](override val singleton: OUT) extends Const[OUT] {
    override lazy val runtimeArity: Int = singleton
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
  class Literal[S <: Int](val singleton: S) extends Const[S] {

    override def runtimeArity: Int = singleton
  }

  object Literal {

    implicit def summon[S <: Int](
        implicit
        w: Witness.Aux[S]
    ): Literal[S] = {
      new Literal[S](w.value)
    }

    def apply(w: Witness.Lt[Int]): Literal[w.T] = {

      Literal.summon[w.T](w)
    }
  }
}
