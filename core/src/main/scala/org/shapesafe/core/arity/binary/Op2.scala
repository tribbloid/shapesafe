package org.shapesafe.core.arity.binary

import org.shapesafe.core.arity.ConstArity
import org.shapesafe.core.arity.ProveArity.|-<
import org.shapesafe.core.arity.Utils.Op
import org.shapesafe.core.arity._
import org.shapesafe.core.debugging.{DebugSymbol, DebugUtil, OpStrs}
import singleton.ops.+

import scala.collection.mutable
import scala.language.implicitConversions

trait Op2 extends Op2Like {

  type Lemma[X1, X2] <: Op
}

object Op2 extends Op2_Imp0 {

  class Impl[
      ??[X1, X2] <: Op,
      SS[A, B] <: DebugSymbol
  ](
      implicit
      sh: Utils.IntSh[??]
  ) extends Op2 {

    override type Lemma[X1, X2] = ??[X1, X2]
    override type Debug[A, B] = SS[A, B]

    case class On[
        A1 <: Arity,
        A2 <: Arity
    ](
        a1: A1,
        a2: A2
    ) extends Conjecture2[A1, A2] {
      // TODO: can this be VerifiedArity?

      override type _Refute =
        DebugUtil.REFUTE.T + OpStrs.Infix[A1, SS[Unit, Unit]#_AsOpStr, A2] + DebugUtil.UNDEFINED.T

      override lazy val runtimeArity: Int = sh.apply(a1.runtimeArity, a2.runtimeArity).getValue
    }

    override def on(a1: ArityAPI, a2: ArityAPI): On[a1._Arity, a2._Arity] = On(a1.arity, a2.arity)
  }

  lazy val cache = mutable.Map.empty[AnyRef, Op2]

  def apply[
      ??[X1, X2] <: Op,
      SS[A, B] <: DebugSymbol
  ](
      implicit
      sh: Utils.IntSh[??]
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
      OP <: Op2
  ](
      implicit
      bound1: A1 |-< ConstArity[S1], // TODO: make it similar to unsafe
      bound2: A2 |-< ConstArity[S2],
      lemma: OP#Lemma[S1, S2]
  ) = {
    ProveArity.forAll[OP#On[A1, A2]].=>> { _ =>
      ConstArity.Derived.summon[OP#Lemma[S1, S2]](lemma)
    }
  }

//  def apply[
//      ??[X1, X2] <: Op,
//      A1 <: ArityCore,
//      A2 <: ArityCore
//  ](
//      a1: A1,
//      a2: A2
//  )(
//      implicit
//      sh: Utils.IntSh[??]
//  ): Op2[??]#On[A1, A2] = {
//
//    val op2 = new Op2[??] // TODO: should be cached
//
//    op2.On(a1, a2)
//  }
}
