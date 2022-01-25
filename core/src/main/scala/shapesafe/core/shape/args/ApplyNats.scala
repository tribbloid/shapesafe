package shapesafe.core.shape.args

import shapesafe.core.shape.StaticShape
import shapesafe.core.shape.Shape.^
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

    type OUB = StaticShape

    override val fromHList: StaticShape.FromNats.type = StaticShape.FromNats

    override type Result[T <: OUB] = ^[T]
    override def toResult[T <: OUB](v: T) = v.^
  }
}
