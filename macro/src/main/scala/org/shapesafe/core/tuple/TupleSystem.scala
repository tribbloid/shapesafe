package org.shapesafe.core.tuple

trait TupleSystem {

  type UpperBound

  type Impl

  type Eye <: Impl
  def Eye: Eye

//  type ><[
//      TAIL <: Impl,
//      HEAD <: UpperBound
//  ] <: Impl

}

object TupleSystem {}
