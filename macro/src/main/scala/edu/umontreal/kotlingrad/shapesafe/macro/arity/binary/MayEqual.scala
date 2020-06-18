package edu.umontreal.kotlingrad.shapesafe.`macro`.arity.binary

import edu.umontreal.kotlingrad.shapesafe.`macro`.arity.Proof.Invar
import edu.umontreal.kotlingrad.shapesafe.`macro`.arity.{Expr, Proof}
import singleton.ops.{==, Require}

trait MayEqual[-A1 <: Proof, -A2 <: Proof] extends Expr {

//  {
//    val numbers = a1.Out.numberOpt.toSeq ++ a2.Out.numberOpt.toSeq
//    require(numbers.distinct.size == 1)
//  }
}

object MayEqual {

  // TODO : both of these signatures are not refined enough, it should contains 4 cases:
  //  Const - Const -> Const[1]
  //  Const - Unsafe -> Const[1]
  //  Unsafe - Const -> Const[2]
  //  Unsafe - Unsafe -> Unsafe

  implicit def unsafe[A1 <: Proof, A2 <: Proof](
      implicit
      domain: A1 UnsafeDomain A2
  ): MayEqual[A1, A2] = {

    domain.ProbablyEqual
  }

  implicit def invar[N1, N2](
      implicit
      domain: InvarDomain[N1, N2],
      lemma: Require[N1 == N2]
  ): MayEqual[Invar[N1], Invar[N2]] with Invar[_] = {

    domain.Equal
  }
}
