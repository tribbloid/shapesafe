package org.shapesafe.core.shape.args

import org.shapesafe.core.shape.ShapeAPI.^
import org.shapesafe.core.shape.{LeafShape, Names}
import shapeless.ops.hlist.Reverse
import shapeless.{HList, SingletonProductArgs}

trait ApplyLiterals extends SingletonProductArgs with ApplyArgs {

  // TODO: should the reverse be justified?
  def applyProduct[H1 <: HList, H2 <: HList](
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

object ApplyLiterals {

  trait ToNames extends ApplyLiterals {

    type OUB = Names

    override val fromHList: Names.FromLiterals.type = Names.FromLiterals

    override type Result[T <: OUB] = T
    override def toResult[T <: OUB](v: T): T = v

  }

  trait ToShape extends ApplyLiterals {

    type OUB = LeafShape

    override val fromHList: LeafShape.FromLiterals.type = LeafShape.FromLiterals

    override type Result[T <: OUB] = ^[T]
    override def toResult[T <: OUB](v: T) = v.^
  }
}
