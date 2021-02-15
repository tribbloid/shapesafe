package org.shapesafe.core.arity.binary

import org.shapesafe.core.arity.LeafArity.Static
import org.shapesafe.core.arity.{Arity, ProveArity}
import org.shapesafe.core.arity.ProveArity.{=>>, ~~>}
import singleton.ops.{==, Require}

trait AssertEqual_Imp0 {

  implicit def unknown[
      A1 <: Arity,
      A2 <: Arity,
      O <: ProveArity.Proof
  ](
      implicit
      domain: UnknownDomain[A1, A2, O]
  ): UnknownDomain[A1, A2, O]#ForEqual = {
    domain.ForEqual
  }
}

object AssertEqual extends AssertEqual_Imp0 with Op2Like {

  case class On[
      +A1 <: Arity,
      +A2 <: Arity
  ](
      a1: A1,
      a2: A2
  ) extends Arity {

    override lazy val runtime: Int = {
      val v1 = a1.runtime
      val v2 = a2.runtime

      require(v1 == v2)
      v1
    }
  }

  implicit def invar[
      A1 <: Arity,
      A2 <: Arity,
      S1,
      S2
  ](
      implicit
      bound1: A1 ~~> Static[S1],
      bound2: A2 ~~> Static[S2],
      lemma: Require[S1 == S2]
  ): InvarDomain[A1, A2, S1, S2]#ForEqual = {

    val domain = InvarDomain[A1, A2, S1, S2]()(bound1, bound2)

    domain.ForEqual()
  }

  override def apply[
      A1 <: Arity,
      A2 <: Arity
  ](a1: A1, a2: A2): On[A1, A2] = {
    On(a1, a2)
  }
}
