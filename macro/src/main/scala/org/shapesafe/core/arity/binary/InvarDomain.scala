package org.shapesafe.core.arity.binary

import org.shapesafe.core.arity.LeafArity.Static
import org.shapesafe.core.arity.Utils.Op
import org.shapesafe.core.arity.{Arity, LeafArity}
import org.shapesafe.core.arity.ProveArity._
import singleton.ops.{==, Require}

import scala.language.higherKinds

case class InvarDomain[
    A1 <: Arity,
    A2 <: Arity,
    S1,
    S2
]()(
    implicit
    bound1: A1 ~~> Static[S1],
    bound2: A2 ~~> Static[S2]
) {

  import org.shapesafe.core.arity.Syntax._

  case class ForOp2[??[X1, X2] <: Op]()(
      implicit
      lemma: S1 ?? S2
  ) extends (Op2[??]#On[A1, A2] =>> OfStatic[S1 ?? S2]) {

    implicit class apply(in: Op2[??]#On[A1, A2]) extends OfStatic[S1 ?? S2] {

      override def out: Out = LeafArity.Derived.summon[S1 ?? S2](lemma)
    }
  }

  case class ForEqual()(
      implicit
      lemma: Require[S1 == S2]
  ) extends (A1 =!= A2 ~~> Static[S1]) {

    def apply(in: A1 =!= A2) = bound1.apply(in.a1)
  }
}
