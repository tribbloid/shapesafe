package edu.umontreal.kotlingrad.shapesafe.arity.binary

import edu.umontreal.kotlingrad.shapesafe.arity.{ArityLike, ArityOp}
import singleton.ops.{==, Require}

trait MayEqual[-A1 <: ArityLike, -A2 <: ArityLike] extends ArityOp {

//  {
//    val numbers = a1.Out.numberOpt.toSeq ++ a2.Out.numberOpt.toSeq
//    require(numbers.distinct.size == 1)
//  }
}

object MayEqual {

  implicit def unsafe[A1 <: ArityLike, A2 <: ArityLike](
      implicit
      lemma: A1 Unsafe A2
  ): A1 MayEqual A2 = {

    lemma.ProbablyEqual
  }

  implicit def const[N1, N2](
      implicit
      lemma: Require[N1 == N2]
  ): ArityLike.Const[N1] MayEqual ArityLike.Const[N2] = {

    val const = new ForConst[N1, N2]()
    new const.Equal()
  }
}
