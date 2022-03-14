package shapesafe.core.shape.ops

import shapesafe.core.shape.{Names, Shape}

trait VectorOps extends HasShape {

  def dot[THAT <: Shape](that: THAT) = {
    val s1 = shape :<<= Names.i
    val s2 = that :<<= Names.i

    s1.einSum(s2) --> Names.i
  }

  def cross[THAT <: Shape](that: THAT) = {

    shape.requireEqual(that).requireEqual(Shape.shape3)
  }
}
