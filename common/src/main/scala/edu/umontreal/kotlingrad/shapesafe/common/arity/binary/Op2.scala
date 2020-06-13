package edu.umontreal.kotlingrad.shapesafe.common.arity.binary

import edu.umontreal.kotlingrad.shapesafe.common.arity.{ArityLike, Expr, IntOp}
import singleton.ops

import scala.language.higherKinds

trait Op2[
    -A1 <: ArityLike,
    -A2 <: ArityLike,
    ConstOp[_, _]
] extends Expr {

  //    type Out <: Arity
  //    def out(a1: A1, a2: A2): Out
}

object Op2 {

  implicit def unsafe[
      A1 <: ArityLike,
      A2 <: ArityLike,
      ConstOp[_, _]
  ](
      implicit lemma: A1 Unsafe A2
  ): Op2[A1, A2, ConstOp] = {

    new lemma.ToUnknown[ConstOp]()
  }

  implicit def const[
      N1 <: IntOp,
      N2 <: IntOp,
      ConstOp[_, _]
  ](
      implicit
      a1: ArityLike.Const[N1],
      a2: ArityLike.Const[N2],
      o: ConstOp[N1, N2] with IntOp
  ): Op2[ArityLike.Const[N1], ArityLike.Const[N2], ConstOp] = {

    type O = ConstOp[N1, N2]

    val const = new ForConst[N1, N2]

    new const.Op2Impl[ConstOp]()
  }

  type +[-A1 <: ArityLike, -A2 <: ArityLike] = Op2[A1, A2, ops.+]

  type -[-A1 <: ArityLike, -A2 <: ArityLike] = Op2[A1, A2, ops.-]

  type *[-A1 <: ArityLike, -A2 <: ArityLike] = Op2[A1, A2, ops.*]

  type /[-A1 <: ArityLike, -A2 <: ArityLike] = Op2[A1, A2, ops./]
}
