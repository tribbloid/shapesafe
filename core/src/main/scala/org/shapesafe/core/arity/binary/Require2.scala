package org.shapesafe.core.arity.binary

import org.shapesafe.core.arity.Utils.Op
import org.shapesafe.core.arity._
import org.shapesafe.core.debugging.Reporters.ForArity
import org.shapesafe.core.debugging.{DebugUtil, HasDebugSymbol}

import scala.collection.mutable

/**
  * Output is always the TIGHTEST TYPE CONSTRAINT of the FIRST argument, no exception
  */
trait Require2 extends Op2Like {

  type Lemma[X1, X2] <: Op
}

object Require2 extends Require2_Imp0 {

  import ProveArity._
  import singleton.ops._

  class Impl[
      ??[X1, X2] <: Op,
      SS[A, B] <: HasDebugSymbol.Require
  ](
      implicit
      sh: Utils.BoolSh[??]
  ) extends Require2 {

    // TODO: this should supersedes AssertEqual

    override type Lemma[X1, X2] = ??[X1, X2]
    override type Debug[A, B] = SS[A, B]

    import singleton.ops._

    case class On[
        A1 <: Arity,
        A2 <: Arity
    ](
        a1: A1,
        a2: A2
    ) extends Conjecture2[A1, A2] {

      override type _Refute =
        DebugUtil.REFUTE.T + A1#_DebugSymbol + SS[Unit, Unit]#Negation#_DebugSymbol + A2#_DebugSymbol

      override lazy val runtimeValue: Int = {
        val v1 = a1.runtimeValue
        val v2 = a2.runtimeValue

        require(sh.apply(v1, v2).getValue, "runtime Requirement failed")
        v1
      }
    }

    override def on(a1: ArityAPI, a2: ArityAPI): On[a1._Arity, a2._Arity] = {
      On(a1.arity, a2.arity)
    }
  }

  lazy val cache = mutable.Map.empty[AnyRef, Require2]

  def apply[
      ??[X1, X2] <: Op,
      SS[A, B] <: HasDebugSymbol.Require
  ](
      implicit
      sh: Utils.BoolSh[??]
  ): Impl[??, SS] = {

    cache
      .getOrElseUpdate(
        sh,
        new Impl[??, SS]()(sh)
      )
      .asInstanceOf[Impl[??, SS]]
  }

  implicit def invar[
      A1 <: Arity,
      A2 <: Arity,
      S1,
      S2,
      OP <: Require2,
      MSG
  ](
      implicit
      bound1: A1 |-< ConstArity[S1],
      bound2: A2 |-< ConstArity[S2],
      refute0: ForArity.Refute0[OP#On[ConstArity[S1], ConstArity[S2]], MSG],
      lemma: RequireMsg[
        OP#Lemma[S1, S2],
        MSG
      ]
  ): OP#On[A1, A2] |- ConstArity[S1] = {
    ProveArity.forAll[OP#On[A1, A2]].=>> { v =>
      bound1.instanceFor(v.a1)
    }
  }
}
