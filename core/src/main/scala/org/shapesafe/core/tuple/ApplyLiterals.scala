package org.shapesafe.core.tuple

import shapeless.ops.hlist.Reverse
import shapeless.{HList, SingletonProductArgs}

trait ApplyLiterals extends SingletonProductArgs {

  val fromHList: TupleSystem#AbstractFromHList

  // TODO: should the reverse be justified?
  def applyProduct[H1 <: HList, H2 <: HList](
      v: H1
  )(
      implicit
      reverse: Reverse.Aux[H1, H2],
      lemma: fromHList.Case[H2]
  ): lemma.Out = {
    lemma.apply(v.reverse)
  }
}