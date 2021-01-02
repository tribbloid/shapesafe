package com.tribbloids.shapesafe.m.shape
import com.tribbloids.shapesafe.m.arity.Expression
import com.tribbloids.shapesafe.m.shape
import com.tribbloids.shapesafe.m.shape.NamedShape.CanName
import shapeless.{::, HNil, Witness}

trait Shape {}

object Shape {

  type Empty = NamedShape[HNil]
  object Empty extends NamedShape(HNil: HNil)

  def ~[
      I <: Expression
  ](dim: I)(
      implicit canName: CanName[I]
  ) = Empty | (dim)
}
