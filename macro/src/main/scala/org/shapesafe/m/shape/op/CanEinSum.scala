package org.shapesafe.m.shape.op

import org.shapesafe.m.shape.Shape
import shapeless.HList
import shapeless.ops.hlist.Prepend

trait CanEinSum[I <: EinSumIndexed.Proto] {

  def children: Seq[Shape]

  val indexed: I

  type Index = indexed.Static
  val index: Index = indexed.static

  def einSum[
      THAT <: Shape,
      H_OUT <: HList
//      OUT <: Shape
  ](that: THAT)(
      implicit
      checkThat: EinSumIndexed.FromStatic.Spec[that.Index], // just for detecting low-level mistakes
      toConcat: Prepend.Aux[that.Index, Index, H_OUT],
      checkConcat: EinSumIndexed.FromStatic.Spec[H_OUT],
      //      toShape: Shape.FromStatic.==>[H_OUT, OUT]
  ) = {

    val concat: H_OUT = that.index ++ index

    val indexed = EinSumIndexed.FromStatic.apply(concat)

    val out = EinSumOps(this.children :+ that)(indexed)

    out
  }
}
