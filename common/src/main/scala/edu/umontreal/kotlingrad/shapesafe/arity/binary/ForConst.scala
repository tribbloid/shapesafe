package edu.umontreal.kotlingrad.shapesafe.arity.binary

import edu.umontreal.kotlingrad.shapesafe.arity.{Arity, ArityLike}
import singleton.ops.{==, Require}

import scala.language.higherKinds

class ForConst[N1, N2]() {

  type A1 = ArityLike.Const[N1]
  type A2 = ArityLike.Const[N2]

  class Equal()(implicit lemma: Require[N1 == N2]) extends (A1 MayEqual A2) {

    override type Out = Arity.Constant[N1]
  }

  class ToUnknown[ConstOp[_, _]]() extends Op2[A1, A2, ConstOp] {

    type Out = Arity.Constant[ConstOp[N1, N2]]
  }
}
