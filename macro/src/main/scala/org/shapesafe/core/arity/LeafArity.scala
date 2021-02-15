package org.shapesafe.core.arity

import com.tribbloids.graph.commons.util.{IDMixin, WideTyped}
import org.shapesafe.core.arity.Utils.{NatAsOp, Op}
import shapeless.{Nat, Witness}
import singleton.ops.{+, ==, Require}

import scala.language.implicitConversions

/**
  * Irreducible
  */
trait LeafArity extends Proven {}

object LeafArity {

  import Witness._

  trait Const[S] extends LeafArity with IDMixin {

    type SS = S
    def singleton: S

    override def _id: S = singleton

    @transient case object internal {

      // for test only
      def _can_+(w: Lt[Int])(
          implicit
          proof: S + w.T
      ): Unit = {}

      def proveSameType[N2](
          implicit
          proof: S =:= N2
      ): Unit = {}

      def proveEqualType[N2](
          implicit
          proof: Require[S == N2]
      ): Unit = {}

      // TODO: should be named proofEqual, require should do everything in runtime?
      def requireEqual(w: Lt[Int])(
          implicit
          proof: Require[SS == w.T]
      ): Unit = {

        proveEqualType[w.T]

        require(w.value == runtime)
      }
    }
  }

  object Const {}

  class Derived[S <: Op](override val singleton: S) extends Const[S] {
    override lazy val runtime: Int = singleton.value.asInstanceOf[Int]
  }

  object Derived {

    implicit def summon[S <: Op](
        implicit
        s: S
    ): Derived[S] = new Derived[S](s)
  }

  // this makes it impossible to construct directly from Int type
  class Literal[S <: Int](val singleton: S) extends Const[S] {

    override def runtime: Int = singleton
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

  object FromNat {

    def apply[N <: Nat](v: N)(
        implicit
        ev: NatAsOp[N]
    ): Derived[NatAsOp[N]] = {

      Derived.summon[NatAsOp[N]](ev)
    }
  }

  def apply(w: Witness.Lt[Int]): Literal[w.T] = {
    Literal.apply(w)
  }

  lazy val _0 = LeafArity(0)

  lazy val _1 = LeafArity(1)

  lazy val _2 = LeafArity(2)

  lazy val _3 = LeafArity(3)

  case object Wide {
    //TODO: redundant, should be merged into above
    // TODO: use a third-party library or selectDynamic to widen this.type
    lazy val _0 = WideTyped(LeafArity(0))

    lazy val _1 = WideTyped(LeafArity(1))

    lazy val _2 = WideTyped(LeafArity(2))

    lazy val _3 = WideTyped(LeafArity(3))
  }

  case class Var(runtime: Int) extends LeafArity {

    final type Out = Var
  }

  trait Unchecked extends LeafArity {

    final type Out = Unchecked
  }

  case object Unchecked extends Unchecked {
    override def runtime: Int = throw new UnsupportedOperationException("<no runtime value>")
  }
}
