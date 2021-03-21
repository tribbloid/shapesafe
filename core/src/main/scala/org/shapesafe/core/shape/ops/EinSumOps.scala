package org.shapesafe.core.shape.ops

import org.shapesafe.core.shape.ShapeAPI.^
import org.shapesafe.core.shape.{Names, Shape, ShapeAPI}
import org.shapesafe.core.shape.binary.OuterProduct
import org.shapesafe.core.shape.unary.{CheckEinSum, Reorder}
import shapeless.{HList, SingletonProductArgs}
import shapeless.ops.hlist.Reverse

case class EinSumOps[
    S1 <: Shape
](
    override val shape: S1
) extends HasShape {

  final override type _Shape = S1

  lazy val checked: CheckEinSum[S1] = CheckEinSum(shape)

  def -->[N <: Names](names: N): ^[Reorder[CheckEinSum[S1], N]] = {

    Reorder(checked, names).^
  }

  object -->* extends SingletonProductArgs {

    def applyProduct[H1 <: HList, H2 <: HList](
        v: H1
    )(
        implicit
        reverse: Reverse.Aux[H1, H2],
        lemma: Names.FromLiterals.Case[H2]
    ): ^[Reorder[CheckEinSum[S1], lemma.Out]] = {

      val names = lemma.apply(reverse(v))
      -->(names)
    }
  }

  def all: ^[CheckEinSum[S1]] = {
    checked.^
  }

//  def build[S2 <: Shape](that: EinSumOps[S2]): CheckEinSum[OuterProduct[CheckEinSum[S1], CheckEinSum[S2]]] = {
//    val direct = checked >< that.checked
//
//    CheckEinSum(direct.shape)
//  }

  def apply[S2 <: Shape](that: ShapeAPI.^[S2]): EinSumOps[OuterProduct[CheckEinSum[S1], CheckEinSum[S2]]] = {
    val direct = checked.^ >< CheckEinSum(that.shape).^

    EinSumOps(direct)
  }

  def einSum: this.type = this
}
