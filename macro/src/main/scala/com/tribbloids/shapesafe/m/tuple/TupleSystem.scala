package com.tribbloids.shapesafe.m.tuple

trait TupleSystem {

  type UpperBound

  type Impl

  protected val _Eye: Impl
  final type Eye = _Eye.type
  def Eye: Eye = _Eye

//  type ><[
//      TAIL <: Impl,
//      HEAD <: UpperBound
//  ] <: Impl

}

object TupleSystem {}
