package shapesafe.core.arity.binary

import shapesafe.core.arity.Utils.Op
import shapesafe.core.arity._
import shapesafe.core.debugging.{DebugConst, HasDebugSymbol}

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
        A1 <: ArityType,
        A2 <: ArityType
    ](
        a1: A1,
        a2: A2
    ) extends Conjecture2[A1, A2] {

      override type _Refute =
        DebugConst.REFUTE.T + A1#_DebugSymbol + SS[Unit, Unit]#Negation#_DebugSymbol + A2#_DebugSymbol

      override lazy val runtimeValue: Int = {
        val v1 = a1.runtimeValue
        val v2 = a2.runtimeValue

        require(sh.apply(v1, v2).getValue, "runtime Requirement failed")
        v1
      }
    }

    override def on(a1: Arity, a2: Arity): On[a1._ArityType, a2._ArityType] = {
      On(a1.arityType, a2.arityType)
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
      A1 <: ArityType,
      A2 <: ArityType,
      S1,
      S2,
      OP <: Require2
  ](
      implicit
      bound1: A1 |-< ConstArity[S1],
      bound2: A2 |-< ConstArity[S2],
      lemma: Require[OP#Lemma[S1, S2]]
  ): OP#On[A1, A2] |- ConstArity[S1] = {
    ProveArity.forAll[OP#On[A1, A2]].=>> { v =>
      bound1.instanceFor(v.a1)
    }
  }
}
