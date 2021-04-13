package org.shapesafe.core.shape.unary

import org.shapesafe.core.shape.ProveShape._
import org.shapesafe.core.shape.{LeafShape, Names, Shape, ShapeConjecture}
import shapeless.ops.hlist.ZipWithKeys
import shapeless.{HList, Witness}

case class |<<-[
    S1 <: Shape,
    N <: Names
](
    s1: S1 with Shape,
    newNames: N
) extends ShapeConjecture {}

object |<<- {

  import org.shapesafe.core.shape.ProveShape.Factory._

  val ss = Witness("dummy dummy")

  implicit def simplify[
      S1 <: Shape,
      P1 <: LeafShape,
      N <: Names,
      HO <: HList
  ](
      implicit
      lemma: S1 |-< P1,
      zip: ZipWithKeys.Aux[N#Static, P1#_Dimensions#Static, HO],
      toShape: LeafShape.FromRecord.Case[HO]
  ): |<<-[S1, N] =>> toShape.Out = {
    forAll[|<<-[S1, N]].=>> { src =>
      val keys: N#Static = src.newNames.static
      val p1: P1 = lemma.valueOf(src.s1)

      val values: P1#_Dimensions#Static = p1.dimensions.static

      val zipped: HO = values.zipWithKeys(keys)
      LeafShape.FromRecord(zipped)
    }
  }

  // TODO: DEAD LOOP!
//  implicit def axiom[
//      P1 <: LeafShape,
//      N <: Names,
//      HO <: HList,
//      O <: LeafShape
//  ](
//      implicit
//      zip: ZipWithKeys.Aux[N#Keys, P1#_Dimensions#Static, HO],
//      toShape: LeafShape.FromRecord.==>[HO, O]
//  ): WithNames[P1, N] =>> O = {
//    from[WithNames[P1, N]].=>> { src =>
//      val keys: N#Keys = src.newNames.keys
//      val p1: P1 = src.s1
//
//      val values: P1#_Dimensions#Static = p1.dimensions.static
//
//      val zipped: HO = values.zipWithKeys(keys)
//      LeafShape.FromRecord(zipped)
//    }
//  }
//
//  implicit def theorem[
//      S1 <: Shape,
//      N <: Names,
//      P1 <: LeafShape,
//      O <: LeafShape
//  ](
//      implicit
//      lemma1: S1 ~~> P1,
//      lemma2: WithNames[P1, N] --> O
//  ): WithNames[S1, N] =>> O = {
//    from[WithNames[S1, N]].=>> { src =>
//      val p1: P1 = lemma1.valueOf(src.s1)
//
//      lemma2.valueOf(
//        src.copy(p1)
//      )
//    }
//  }
}
