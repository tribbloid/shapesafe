package org.shapesafe.core.arity.binary

import org.shapesafe.core.arity.Const
import org.shapesafe.core.arity.ProveArity.|-<
import org.shapesafe.core.arity.Utils.Op
import org.shapesafe.core.arity.{Arity, ArityAPI, ProveArity, Utils}
import org.shapesafe.core.debugging.Reporters.ForArity
import org.shapesafe.core.debugging.{DebugSymbol, DebugUtil, OpStrs}

import scala.collection.mutable

/**
  * Output is always the TIGHTEST TYPE CONSTRAINT of the FIRST argument, no exception
  */
trait Require2 extends Op2Like {

  type Lemma[X1, X2] <: Op
}

object Require2 extends Require2_Imp0 {

  import ProveArity.Factory._
  import singleton.ops._

  class Impl[
      ??[X1, X2] <: Op,
      SS[A, B] <: DebugSymbol.Require
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
        DebugUtil.REFUTE.T + OpStrs.Infix[A1, SS[Unit, Unit]#Complement#_AsOpStr, A2]

      override lazy val runtimeArity: Int = {
        val v1 = a1.runtimeArity
        val v2 = a2.runtimeArity

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
      SS[A, B] <: DebugSymbol.Require
  ](
      implicit
      sh: Utils.BoolSh[??]
  ): Impl[??, SS] = {

    cache
      .getOrElseUpdate(
        sh,
        new Impl[??, SS]
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
      bound1: A1 |-< Const[S1],
      bound2: A2 |-< Const[S2],
      refute0: ForArity.Refute0[OP#On[Const[S1], Const[S2]], MSG],
      lemma: RequireMsg[
        OP#Lemma[S1, S2],
        MSG
      ]
  ): OP#On[A1, A2] =>> Const[S1] = {
    ProveArity.forAll[OP#On[A1, A2]].=>> { v =>
      bound1.valueOf(v.a1)
    }
  }
}
