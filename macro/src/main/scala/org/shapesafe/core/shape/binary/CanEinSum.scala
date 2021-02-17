package org.shapesafe.core.shape.binary

import org.shapesafe.core.shape.LeafShape
import shapeless.HList
import shapeless.ops.hlist.Prepend

trait CanEinSum[I <: EinSumIndexed.Proto] {

  def children: Seq[LeafShape]

  val indexed: I

  type Index = indexed.Static
  val index: Index = indexed.static

  def einSum[
      THAT <: LeafShape,
      H_OUT <: HList
  ](that: THAT)(
      implicit
      checkThat: EinSumIndexed.FromStatic.Case[that.Record], // just for detecting low-level mistakes
      toConcat: Prepend.Aux[that.Record, Index, H_OUT],
      checkConcat: EinSumIndexed.FromStatic.Case[H_OUT]
  ) = {

    val concat: H_OUT = that.record ++ index

    val indexed = EinSumIndexed.FromStatic.apply(concat)

    val out = EinSumOps(this.children :+ that)(indexed)

    out
  }
}
