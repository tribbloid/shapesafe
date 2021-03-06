package org.shapesafe.core.shape.args

import org.shapesafe.core.shape.LeafShape
import org.shapesafe.core.shape.ShapeAPI.^
import shapeless.ops.hlist.Reverse
import shapeless.{HList, NatProductArgs}

trait ApplyNats extends ApplyArgs with NatProductArgs {

  // TODO: should the reverse be justified?
  def applyNatProduct[H1 <: HList, H2 <: HList](
      v: H1
  )(
      implicit
      reverse: Reverse.Aux[H1, H2],
      lemma: fromHList.Case[H2]
  ): Result[lemma.Out] = {
    val out = lemma.apply(v.reverse)
    toResult(out)
  }
}

object ApplyNats {

  trait ToShape extends ApplyNats {

    type OUB = LeafShape

    override val fromHList: LeafShape.FromNats.type = LeafShape.FromNats

    override type Result[T <: OUB] = ^[T]
    override def toResult[T <: OUB](v: T) = v.^
  }
}
