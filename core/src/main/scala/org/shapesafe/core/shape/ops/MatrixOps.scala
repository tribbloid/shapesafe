package org.shapesafe.core.shape.ops

import org.shapesafe.core.shape.{Shape, ShapeAPI}

trait MatrixOps extends HasShape {

  import org.shapesafe.core.shape.Names._

  def :*[THAT <: ShapeAPI](that: THAT) = {
    val s1 = api |<<- ij
    val s2 = that |<<- jk

    s1.einSum(s2) --> ik

  }
}
