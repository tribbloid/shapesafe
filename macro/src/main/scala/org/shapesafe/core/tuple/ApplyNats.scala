package org.shapesafe.core.tuple

import shapeless.ops.hlist.Reverse
import shapeless.{HList, NatProductArgs}

trait ApplyNats extends NatProductArgs {

  val fromHList: CanFromStatic#AbstractFromHList

  // TODO: should the reverse be justified?
  def applyNatProduct[H1 <: HList, H2 <: HList](
      v: H1
  )(
      implicit
      reverse: Reverse.Aux[H1, H2],
      lemma: fromHList.Case[H2]
  ): lemma.Out = {
    lemma.apply(v.reverse)
  }
}
