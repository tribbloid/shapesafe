package org.shapesafe.core.arity.binary

import org.shapesafe.core.arity.LeafArity.Const
import org.shapesafe.core.arity.ProveArity.|-<
import org.shapesafe.core.arity.Utils.Op
import org.shapesafe.core.arity._
import org.shapesafe.core.debugging.InfoCT
import org.shapesafe.core.debugging.InfoCT.Peek
import singleton.ops.+

import scala.language.implicitConversions

class Op2[
    ??[X1, X2] <: Op,
    Sym <: String
](
    implicit
    sh: Utils.IntSh[??]
) extends Op2Like {

  case class On[
      A1 <: Arity,
      A2 <: Arity
  ](
      a1: A1,
      a2: A2
  ) extends Conjecture2[A1, A2] {
    // TODO: can this be VerifiedArity?

    override type _Peek = Peek[A1] + Sym + Peek[A2]

    override type _Refute = InfoCT.noCanDo.T + InfoCT.nonExisting.T + _Peek

    override lazy val runtimeArity: Int = sh.apply(a1.runtimeArity, a2.runtimeArity).getValue
  }

  override def on(a1: ArityAPI, a2: ArityAPI): On[a1._Arity, a2._Arity] = On(a1.arity, a2.arity)
}

trait Op2_Imp0 {

  implicit def unchecked[
      A1 <: Arity,
      A2 <: Arity,
      O <: ProveArity.Term,
      ??[X1, X2] <: Op
  ](
      implicit
      domain: UncheckedDomain[A1, A2, O],
      sh: Utils.IntSh[??]
  ) = {
    domain.forOp2[??]
  }
}

object Op2 extends Op2_Imp0 {

  type UB[
      ??[X1, X2] <: Op
  ] = Op2[??, _ <: String]

  implicit def invar[
      A1 <: Arity,
      A2 <: Arity,
      S1,
      S2,
      ??[X1, X2] <: Op
  ](
      implicit
      bound1: A1 |-< Const[S1], // TODO: make it similar to unsafe
      bound2: A2 |-< Const[S2],
      lemma: S1 ?? S2
  ) = {
    val domain = InvarDomain[A1, A2, S1, S2]()(bound1, bound2)

    domain.forOp2[??]
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
