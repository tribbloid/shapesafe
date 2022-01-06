package shapesafe.core.shape.ops

import shapesafe.core.shape.ShapeAPI

trait MatrixOps extends HasShape {

  import shapesafe.core.shape.Const._

  def matMul[THAT <: ShapeAPI](that: THAT) = {
    val s1 = api |<<- ij
    val s2 = that |<<- jk

    s1.einSum(s2) --> ik
  }

  def mat_*[THAT <: ShapeAPI](that: THAT) = {
    matMul[that.type](that)
  }
}
