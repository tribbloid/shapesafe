//package org.shapesafe.core.shape
//
//import ProveShape._
//
//trait VerifiedShape extends Shape {}
//
//object VerifiedShape {
//
//  implicit def endo[T <: VerifiedShape]: T =>> T = ProveShape.forAll[T].=>>(identity[T])
//
//  //  abstract class ProvenAs[O <: LeafArity]()(
//  //      implicit
//  //      val out: O
//  //  ) extends Proven
//  //      with ProveArity.Of[O] {}
//
//}
