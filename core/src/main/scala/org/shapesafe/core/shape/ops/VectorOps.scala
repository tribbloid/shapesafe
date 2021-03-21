package org.shapesafe.core.shape.ops

import org.shapesafe.core.shape.{Shape, ShapeAPI}

trait VectorOps extends HasShape {

  import org.shapesafe.core.shape.Names._

  def dot[THAT <: ShapeAPI](that: THAT) = {
    val s1 = api |<<- i
    val s2 = that |<<- i

    s1.einSum(s2) -->* "i"
  }
}
