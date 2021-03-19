//package org.shapesafe.core.shape.binary
//
//import org.shapesafe.core.arity.binary.Op2Like
//import org.shapesafe.core.shape.{LeafShape, Shape}
//import shapeless.HList
//import shapeless.ops.hlist.ZipWith
//
//// TODO: This is an over-specific case of OuterProduct + unary.Flatten, remove?
//case class ElementWise2[
//    OP <: Op2Like,
//    S1 <: Shape,
//    S2 <: Shape
//](
//    op: OP,
//    s1: S1,
//    s2: S2
//) {}
//
//object ElementWise2 {
//
//  import org.shapesafe.core.shape.ProveShape._
//  import Factory._
//
//  implicit def zip[
//      OP <: Op2Like,
//      S1 <: Shape,
//      P1 <: LeafShape,
//      S2 <: Shape,
//      P2 <: LeafShape,
//      HO <: HList
//  ](
//      implicit
//      lemma1: S1 |-< P1,
//      lemma2: S2 |-< P2,
//      zip: ZipWith.Aux[P1#_Dimensions#Static, P2#_Dimensions#Static, OP#AsShapelessPoly2, HO],
//      toShape: LeafShape.FromRecord.Case[HO]
//  ): ElementWise2[OP, S1, S2] =>> toShape.Out = forAll[ElementWise2[OP, S1, S2]].=>> { v =>
//    val p1: P1 = lemma1.valueOf(v.s1)
//    val p2: P2 = lemma2.valueOf(v.s2)
//
//    val zipped: HO = zip.apply(p1.dimensions.static, p2.dimensions.static)
//
//    toShape(zipped)
//  }
//}
