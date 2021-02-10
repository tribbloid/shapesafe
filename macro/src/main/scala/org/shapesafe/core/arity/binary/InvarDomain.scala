package org.shapesafe.core.arity.binary

import org.shapesafe.core.arity.Utils.Op
import org.shapesafe.core.arity.{Arity, Leaf}
import org.shapesafe.core.arity.ProveArity._
import singleton.ops.{==, Require}

import scala.language.higherKinds

case class InvarDomain[
    A1 <: Arity,
    A2 <: Arity,
    S1,
    S2,
]()(
    implicit
    bound1: A1 ~~> OfStaticImpl[S1],
    bound2: A2 ~~> OfStaticImpl[S2],
) {

  case class ForOp2[??[X1, X2] <: Op]()(
      implicit lemma: S1 ?? S2
  ) extends (Op2[A1, A2, ??] ForAll OfStaticImpl[S1 ?? S2]) {

    case class prove(in: Op2[A1, A2, ??]) extends OfStaticImpl[S1 ?? S2] {

      override def out: Out = Leaf.Derived.summon[S1 ?? S2](lemma)
    }
  }

  case class ForEqual()(
      implicit lemma: Require[S1 == S2]
  ) extends (AssertEqual[A1, A2] ~~> OfStaticImpl[S1]) {

    def apply(in: AssertEqual[A1, A2]): OfStaticImpl[S1] = bound1.apply(in.a1)
  }
}
