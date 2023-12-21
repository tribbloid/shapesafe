package shapesafe.core.shape.ops

import shapeless.ops.hlist.Reverse
import shapeless.{HList, SingletonProductArgs}
import shapesafe.core.shape.Shape.^
import shapesafe.core.shape.unary.{CheckEinSum, Rearrange}
import shapesafe.core.shape.{Names, Shape, ShapeType}

case class EinSumOp[
    S1 <: ShapeType
](
    override val shapeType: S1
) extends HasShape {

  final override type _ShapeType = S1

  lazy val checked: CheckEinSum[S1] = CheckEinSum(shapeType)

  def -->[N <: Names](names: N): ^[Rearrange[CheckEinSum[S1], N]] = {

    Rearrange(checked, names).^
  }

  object -->* extends SingletonProductArgs {

    def applyProduct[H1 <: HList, H2 <: HList, R <: Names](
        v: H1
    )(
        implicit
        reverse: Reverse.Aux[H1, H2],
        lemma: Names.FromLiterals.=>>[H2, R]
    ): ^[Rearrange[CheckEinSum[S1], R]] = {

      val names = lemma.apply(reverse(v))
      -->(names)
    }
  }

  def all: ^[CheckEinSum[S1]] = {
    checked.^
  }

  def apply[S2 <: ShapeType](that: Shape.^[S2]) = {
    val direct = this.shapeType.^ >< that.shapeType.^

    EinSumOp(direct.shapeType)
  }

  def einSum: this.type = this
}
