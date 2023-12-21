package shapesafe.core.shape.args

import shapesafe.core.shape.Shape.^
import shapesafe.core.shape.{Names, StaticShape}
import shapeless.ops.hlist.Reverse
import shapeless.{HList, SingletonProductArgs}

trait ApplyLiterals extends SingletonProductArgs with ApplyArgs {

  // TODO: should the reverse be justified?
  def applyProduct[H1 <: HList, H2 <: HList, R <: ApplyLiterals.this.OUB](
      v: H1
  )(
      implicit
      reverse: Reverse.Aux[H1, H2],
      lemma: fromHList.=>>[H2, R]
  ): Result[R] = {
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

    type OUB = StaticShape

    override val fromHList: StaticShape.FromXInts.type = StaticShape.FromXInts

    override type Result[T <: OUB] = ^[T]
    override def toResult[T <: OUB](v: T) = v.^
  }
}
