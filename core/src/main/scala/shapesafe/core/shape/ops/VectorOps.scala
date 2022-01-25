package shapesafe.core.shape.ops

import shapesafe.core.shape.{Names, Shape}

trait VectorOps extends HasShape {

  import shapesafe.core.shape.Const._

  def dot[THAT <: Shape](that: THAT) = {
    val s1 = shape :<<= i
    val s2 = that :<<= i

    s1.einSum(s2) --> Names.Eye
  }

  def cross[THAT <: Shape](that: THAT) = {

    shape.requireEqual(that).requireEqual(shape3)
  }
}
