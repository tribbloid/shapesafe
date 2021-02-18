package org.shapesafe.core.shape.unary

import org.shapesafe.core.shape.ProveShape._
import org.shapesafe.core.shape.{LeafShape, Names, Shape}
import shapeless.HList
import shapeless.ops.hlist.ZipWithKeys

case class WithNames[
    S1 <: Shape,
    N <: Names
](
    s1: S1,
    newNames: N
) extends Shape {}

object WithNames {

  implicit def asLeaf[
      S1 <: Shape,
      P1 <: LeafShape,
      N <: Names,
      HO <: HList,
      O <: LeafShape
  ](
      implicit
      lemma: S1 ~~> P1,
      zip: ZipWithKeys.Aux[N#Keys, P1#_Dimensions#Static, HO],
      toShape: LeafShape.FromRecord.==>[HO, O]
  ): WithNames[S1, N] =>> O = {
    from[WithNames[S1, N]].=>> { src =>
      val keys: N#Keys = src.newNames.keys
      val p1: P1 = lemma(src.s1).out

      val values: P1#_Dimensions#Static = p1.dimensions.static

      val zipped: HO = values.zipWithKeys(keys)
      LeafShape.FromRecord(zipped)
    }
  }
}
