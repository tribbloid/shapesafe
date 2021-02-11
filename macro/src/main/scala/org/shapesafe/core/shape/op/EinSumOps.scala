package org.shapesafe.core.shape.op

import org.shapesafe.core.shape.{Names, Shape}
import org.shapesafe.core.util.RecordUtils
import shapeless.HList
import shapeless.ops.hlist.Mapper

case class EinSumOps[I <: EinSumIndexed.Proto](
    children: Seq[Shape]
)(
    implicit
    override val indexed: I
) extends CanEinSum[I] {

  lazy val n_d: Index = indexed.static

  object GetField extends RecordUtils.GetField(indexed.static)

  def -->[H_OUT <: HList](names: Names.Impl)(
      implicit
      mapper: Mapper.Aux[GetField.type, names.Static, H_OUT],
      toShape: Shape.FromIndex.Spec[H_OUT]
  ): toShape.Out = {

    val projected = names.static.map(GetField)

    Shape.FromIndex(projected)
  }
}
