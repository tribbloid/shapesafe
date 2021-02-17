package org.shapesafe.core.shape.unary

import org.shapesafe.core.shape.ProveShape._
import org.shapesafe.core.shape.{LeafShape, Names, Shape}
import shapeless.HList
import shapeless.ops.hlist.ZipWithKeys

case class Rename[
    S1 <: Shape,
    N <: Names
](
    s1: S1,
    newNames: N
) extends Shape {}

object Rename {

  implicit def asLeaf[
      S1 <: Shape,
      P1 <: LeafShape,
      N <: Names,
      ZZ <: HList,
      O <: LeafShape
  ](
      implicit
      lemma: S1 ~~> P1,
      zipping: ZipWithKeys.Aux[N#Keys, P1#_Dimensions#Static, ZZ],
      restoring: LeafShape.FromRecord.==>[ZZ, O]
  ): Rename[S1, N] =>> O = {
    from[Rename[S1, N]].out { src =>
      val keys: N#Keys = src.newNames.keys
      val p1: P1 = lemma(src.s1).out

      val values: P1#_Dimensions#Static = p1.dimensions.static

      val zipped: ZZ = values.zipWithKeys(keys)
      LeafShape.FromRecord(zipped)
    }
  }
}
