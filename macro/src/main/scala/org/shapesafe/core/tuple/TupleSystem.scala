package org.shapesafe.core.tuple

trait TupleSystem {

  type UpperBound

  type Impl

  protected val eye: Impl
  final type Eye = eye.type
  final def Eye: Eye = eye

//  type ><[
//      TAIL <: Impl,
//      HEAD <: UpperBound
//  ] <: Impl

}

object TupleSystem {}
