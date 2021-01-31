package com.tribbloids.shapesafe.m.shape.op

import com.tribbloids.shapesafe.m.shape.{Names, Shape}
import shapeless.{HList, Poly1}
import shapeless.labelled.FieldType
import shapeless.ops.hlist.Mapper

case class EinSumOps[I <: EinSumIndexed.Proto](
    children: Seq[Shape]
)(
    implicit override val indexed: I
) extends CanEinSum[I] {

  lazy val n_d: Index = indexed.static

  object Extractor extends Poly1 {

    implicit def atFieldType[F, V](
        implicit wk: shapeless.Witness.Aux[F]
    ): Case.Aux[FieldType[F, V], V] = at[FieldType[F, V]] { f =>
      f: V
//      wk.value.toString
    }
  }

  def -->[H_OUT <: HList](names: Names.Impl)(
      implicit
      mapper: Mapper.Aux[Extractor.type, names.Static, H_OUT],
      toShape: Shape.FromIndex.Spec[H_OUT]
  ): toShape.Out = {

    val projected = names.static.map(Extractor)

    Shape.FromIndex(projected)
  }
}
