package edu.umontreal.kotlingrad.shapesafe.common.arity.binary

import edu.umontreal.kotlingrad.shapesafe.common.arity.{Arity, ArityLike, Expr, IntOp}
import singleton.ops.{==, Require}

import scala.language.higherKinds

class ForConst[N1 <: IntOp, N2 <: IntOp]()(
    implicit
    val a1: ArityLike.Const[N1],
    val a2: ArityLike.Const[N2]
) {

  type A1 = ArityLike.Const[N1]
  type A2 = ArityLike.Const[N2]

  implicit val n1: Arity.Constant[N1] = a1.Out
  implicit val n2: Arity.Constant[N2] = a2.Out

  class Equal()(
      implicit lemma: Require[N1 == N2]
  ) extends (A1 MayEqual A2) {

    override type Out = a1.Out
    override def Out: Out = a1.Out
  }

  class Op2Impl[ConstOp[_, _]]()(
      implicit
      o: ConstOp[N1, N2] with IntOp
  ) extends Expr.Out[Arity.FromOp[ConstOp[N1, N2] with IntOp]]()(Arity.FromOp.summon(o))
      with Op2[A1, A2, ConstOp] {}
}
