package org.shapesafe.core.arity.binary

import org.shapesafe.core.arity.LeafArity.Const
import org.shapesafe.core.arity.ProveArity._
import org.shapesafe.core.arity.Utils.Op
import org.shapesafe.core.arity.ops.ArityOps.=!=
import org.shapesafe.core.arity.{Arity, LeafArity, ProveArity}
import singleton.ops.{==, Require}

import scala.language.higherKinds

case class InvarDomain[
    A1 <: Arity,
    A2 <: Arity,
    S1,
    S2
]()(
    implicit
    bound1: A1 |~~ Const[S1],
    bound2: A2 |~~ Const[S2]
) {

  def forOp2[??[X1, X2] <: Op](
      implicit
      lemma: S1 ?? S2
  ): Op2[??]#On[A1, A2] =>> LeafArity.Derived[S1 ?? S2] = ProveArity.forAll[Op2[??]#On[A1, A2]].=>> { v =>
    LeafArity.Derived.summon[S1 ?? S2](lemma)
  }

  def forEqual(
      implicit
      lemma: Require[S1 == S2]
  ): A1 =!= A2 =>> Const[S1] = ProveArity.forAll[A1 =!= A2].=>> { v =>
    bound1.valueOf(v.a1)
  }
}
