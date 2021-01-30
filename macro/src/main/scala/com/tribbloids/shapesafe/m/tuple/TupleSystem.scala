package com.tribbloids.shapesafe.m.tuple

trait TupleSystem {

  type UpperBound

  type Impl

  protected val eye: Impl
  final type Eye = eye.type
  def Eye: Eye = eye

//  type ><[
//      TAIL <: Impl,
//      HEAD <: UpperBound
//  ] <: Impl

}

object TupleSystem {}
