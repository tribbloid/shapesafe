package org.shapesafe.core.shape.unary

import org.shapesafe.core.shape.ProveShape._
import org.shapesafe.core.shape._
import shapeless.HList
import shapeless.ops.hlist.Mapper

case class Rearrange[ // last step of einsum, contract, transpose, etc.
    S1 <: Shape,
    II <: Indices
](
    s1: S1,
    indices: II
) extends Shape {}

object Rearrange {

  implicit def asLeaf[
      S1 <: Shape,
      P1 <: LeafShape,
      II <: Indices,
      HO <: HList,
      O <: LeafShape
  ](
      implicit
      lemma: S1 ~~> P1,
      mapper: Mapper.Aux[P1#IndexLookup, II#Static, HO],
      toShape: LeafShape.FromRecord.==>[HO, O]
  ): Rearrange[S1, II] =>> O = {
    ProveShape.from[Rearrange[S1, II]].=>> { rr =>
      val mapped = mapper.apply(rr.indices.static)
      toShape(mapped)
    }
  }
}
