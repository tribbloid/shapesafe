package shapesafe.core.arity.binary

import shapesafe.core.arity.ConstArity.Derived
import shapesafe.core.arity.Utils.Op
import shapesafe.core.arity.{ProveArity, _}
import shapesafe.core.debugging.{DebugUtil, HasDebugSymbol}
import singleton.ops.+

import scala.collection.mutable

trait Op2 extends Op2Like {

  type Lemma[X1, X2] <: Op
}

object Op2 extends Op2_Imp0 {

  import shapesafe.core.arity.ProveArity._

  class Impl[
      ??[X1, X2] <: Op,
      SS[A, B] <: HasDebugSymbol
  ]()(
      implicit
      sh: Utils.IntSh[??]
  ) extends Op2 {

    override type Lemma[X1, X2] = ??[X1, X2]
    override type Debug[A, B] = SS[A, B]

    case class On[
        A1 <: ArityType,
        A2 <: ArityType
    ](
        a1: A1,
        a2: A2
    ) extends Conjecture2[A1, A2] {
      // TODO: can this be VerifiedArity?

      override type _Refute =
        DebugUtil.REFUTE.T + DebugUtil.ILLEGAL_OP.T + A1#_DebugSymbol + SS[Unit, Unit]#_DebugSymbol + A2#_DebugSymbol

      override lazy val runtimeValue: Int = sh.apply(a1.runtimeValue, a2.runtimeValue).getValue
    }

    override def on(a1: Arity, a2: Arity): On[a1._ArityType, a2._ArityType] = On(a1.arityType, a2.arityType)
  }

  lazy val cache = mutable.Map.empty[AnyRef, Op2]

  def apply[
      ??[X1, X2] <: Op,
      SS[A, B] <: HasDebugSymbol
  ](
      implicit
      sh: Utils.IntSh[??]
  ): Impl[??, SS] = {

    cache
      .getOrElseUpdate(
        sh,
        new Impl[??, SS]()(sh)
      )
      .asInstanceOf[Impl[??, SS]]
  }

  implicit def invar[
      A1 <: ArityType,
      A2 <: ArityType,
      S1,
      S2,
      OP <: Op2
  ](
      implicit
      bound1: A1 |-< ConstArity[S1], // TODO: make it similar to unsafe
      bound2: A2 |-< ConstArity[S2],
      lemma: OP#Lemma[S1, S2]
  ): ProveArity.Theorem[OP#On[A1, A2] |- Derived[OP#Lemma[S1, S2], lemma.OutInt]] = {
    ProveArity.forAll[OP#On[A1, A2]].=>> { _ =>
      Derived.summon[OP#Lemma[S1, S2]](lemma)
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
