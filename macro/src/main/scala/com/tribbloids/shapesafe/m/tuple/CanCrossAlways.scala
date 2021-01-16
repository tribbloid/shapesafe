package com.tribbloids.shapesafe.m.tuple

import scala.language.implicitConversions

trait CanCrossAlways[UB] {
  _self: CanonicalTupleSystem[UB] =>

  implicit def canCrossAlways[TAIL <: Impl, HEAD <: UB]: CanCross[TAIL, HEAD] = { (tail, head) =>
    new ><(tail, head)
  }
}