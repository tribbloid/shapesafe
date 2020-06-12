package edu.umontreal.kotlingrad.shapesafe.arity.binary

import edu.umontreal.kotlingrad.shapesafe.arity.{ArityLike, ArityOp}
import singleton.ops

import scala.language.higherKinds

trait Op2[
    -A1 <: ArityLike,
    -A2 <: ArityLike,
    ConstOp[_, _]
] extends ArityOp {

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
      N1,
      N2,
      ConstOp[_, _]
  ]: Op2[ArityLike.Const[N1], ArityLike.Const[N2], ConstOp] = {

    val const = new ForConst[N1, N2]

    new const.ToUnknown[ConstOp]()
  }

  type +[-A1 <: ArityLike, -A2 <: ArityLike] = Op2[A1, A2, ops.+]

  type -[-A1 <: ArityLike, -A2 <: ArityLike] = Op2[A1, A2, ops.-]

  type *[-A1 <: ArityLike, -A2 <: ArityLike] = Op2[A1, A2, ops.*]

  type /[-A1 <: ArityLike, -A2 <: ArityLike] = Op2[A1, A2, ops./]
}
