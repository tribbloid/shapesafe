package shapesafe.core.shape.ops

import shapesafe.core.shape.Shape.^
import shapesafe.core.shape.{Names, Shape, ShapeType}
import shapesafe.core.shape.binary.OuterProduct
import shapesafe.core.shape.unary.{CheckEinSum, Select}
import shapeless.{HList, SingletonProductArgs}
import shapeless.ops.hlist.Reverse

case class EinSumOp[
    S1 <: ShapeType
](
    override val shapeType: S1
) extends HasShape {

  final override type _ShapeType = S1

  lazy val checked: CheckEinSum[S1] = CheckEinSum(shapeType)

  def -->[N <: Names](names: N): ^[Select[CheckEinSum[S1], N]] = {

    Select(checked, names).^
  }

  object -->* extends SingletonProductArgs {

    def applyProduct[H1 <: HList, H2 <: HList](
        v: H1
    )(
        implicit
        reverse: Reverse.Aux[H1, H2],
        lemma: Names.FromLiterals.Case[H2]
    ): ^[Select[CheckEinSum[S1], lemma.Out]] = {

      val names = lemma.apply(reverse(v))
      -->(names)
    }
  }

  def all: ^[CheckEinSum[S1]] = {
    checked.^
  }

  def apply[S2 <: ShapeType](that: Shape.^[S2]): EinSumOp[OuterProduct[CheckEinSum[S1], CheckEinSum[S2]]] = {
    val direct = checked.^ >< CheckEinSum(that.shapeType).^

    EinSumOp(direct)
  }

  def einSum: this.type = this
}
