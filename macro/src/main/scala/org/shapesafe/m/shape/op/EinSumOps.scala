package org.shapesafe.m.shape.op

import org.shapesafe.m.shape.{Names, Shape}
import org.shapesafe.m.util.RecordUtils
import shapeless.HList
import shapeless.ops.hlist.Mapper

case class EinSumOps[I <: EinSumIndexed.Proto](
    children: Seq[Shape]
)(
    implicit override val indexed: I
) extends CanEinSum[I] {

  lazy val n_d: Index = indexed.static

  object getField extends RecordUtils.GetField(indexed.static)

  def -->[H_OUT <: HList](names: Names.Impl)(
      implicit
      mapper: Mapper.Aux[getField.type, names.Static, H_OUT],
      toShape: Shape.FromIndex.Spec[H_OUT]
  ): toShape.Out = {

    val projected = names.static.map(getField)

    Shape.FromIndex(projected)
  }
}
