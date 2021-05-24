package org.shapesafe.core.arity.binary

import org.shapesafe.core.arity.LeafArity.Const
import org.shapesafe.core.arity.Utils.Op
import org.shapesafe.core.arity.ops.ArityOps.:==!
import org.shapesafe.core.arity.{Arity, ArityAPI, ProveArity}
import org.shapesafe.core.debugging.OpStr.ForArity
import org.shapesafe.core.debugging.{DebuggingUtil, Expr, OpStr}

trait Require[
    ??[X1, X2] <: Op,
    SS[A, B] <: Expr.HasLiteral
] {}

trait AssertEqual_Imp0 {

  implicit def unchecked[
      A1 <: Arity,
      A2 <: Arity,
      O <: ProveArity.Term
  ](
      implicit
      domain: UncheckedDomain[A1, A2, O]
  ): ProveArity.Proof[A1 :==! A2, O] = {
    domain.forEqual
  }
}

object AssertEqual extends Op2Like with AssertEqual_Imp0 {

  import ProveArity.Factory._
  import singleton.ops._

  override type Symbol[A, B] = Expr.==[A, B]

  case class On[
      A1 <: Arity,
      A2 <: Arity
  ](
      a1: A1,
      a2: A2
  ) extends Conjecture2[A1, A2] {

    override type _Refute = DebuggingUtil.REFUTE.T + OpStr.Infix[A1, " != ", A2]

    override lazy val runtimeArity: Int = {
      val v1 = a1.runtimeArity
      val v2 = a2.runtimeArity

      require(v1 == v2)
      v1
    }
  }

  override def on(a1: ArityAPI, a2: ArityAPI): On[a1._Arity, a2._Arity] = {
    On(a1.arity, a2.arity)
  }

  import org.shapesafe.core.arity.ProveArity.|-<

  implicit def invar[
      A1 <: Arity,
      A2 <: Arity,
      S1,
      S2
  ](
      implicit
      bound1: A1 |-< Const[S1],
      bound2: A2 |-< Const[S2],
      lemma: RequireMsg[
        S1 == S2,
        ForArity.Refute0[Const[S1] :==! Const[S2]]
      ]
  ): (A1 :==! A2) =>> Const[S1] = {

    val domain = InvarDomain[A1, A2, S1, S2]()(bound1, bound2)

    domain.forEqual(lemma)
  }
}
