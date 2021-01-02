package com.tribbloids.shapesafe.m.shape
import com.tribbloids.shapesafe.m.arity.Expression
import shapeless.{::, HNil}

trait Shape {}

object Shape {

  type Empty = NamedShape[HNil]
  object Empty extends NamedShape(HNil: HNil)

  def |>[N <: Expression.Name, V <: Expression](
      dim: Expression.Named[N, V]
  ) = Empty cross dim
}
