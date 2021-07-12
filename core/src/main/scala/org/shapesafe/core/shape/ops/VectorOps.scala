package org.shapesafe.core.shape.ops

import org.shapesafe.core.shape.{Names, ShapeAPI}

trait VectorOps extends HasShape {

  import org.shapesafe.core.shape.Const._

  def dot[THAT <: ShapeAPI](that: THAT) = {
    val s1 = api |<<- i
    val s2 = that |<<- i

    s1.einSum(s2) --> Names.Eye
  }

  def cross[THAT <: ShapeAPI](that: THAT) = {

    api.requireEqual(that).requireEqual(shape3)
  }
}
