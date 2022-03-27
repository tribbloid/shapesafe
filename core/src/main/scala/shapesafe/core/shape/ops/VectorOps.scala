package shapesafe.core.shape.ops

import shapesafe.core.shape.{Names, Shape}

trait VectorOps extends HasShape {

  def dot[THAT <: Shape](that: THAT) = {
//    val s1 = shape :<<= Names.i
//    val s2 = that :<<= Names.i
//
//    (s1.einSum(s2) --> Names.Eye) >< Shape(1)

    // TODO: above is slight slower, need to optimise

    val outer = (shape >< that) :<<= Names.ii
    (outer.einSum --> Names.Eye) >< Shape._1
  }

  def cross[THAT <: Shape](that: THAT) = {

    shape.requireEqual(that).requireEqual(Shape._3)
  }
}
