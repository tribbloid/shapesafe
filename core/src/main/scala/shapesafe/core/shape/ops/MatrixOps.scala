package shapesafe.core.shape.ops

import shapesafe.core.shape.Shape

trait MatrixOps extends HasShape {

  import shapesafe.core.shape.Const._

  def matMul[THAT <: Shape](that: THAT) = {
    val s1 = shape :<<= ij
    val s2 = that :<<= jk

    s1.einSum(s2) --> ik
  }

  def mat_*[THAT <: Shape](that: THAT) = {
    matMul[that.type](that)
  }
}
