package shapesafe.core.shape.ops

import shapeless.Nat
import shapesafe.core.shape.{Index, Indices, Shape}

trait MatrixOps extends HasShape {

  import shapesafe.core.Const._

  def matMul[THAT <: Shape](that: THAT) = {
    val s1 = shape :<<= ij
    val s2 = that :<<= jk

    s1.einSum(s2) --> ik
  }

  def mat_*[THAT <: Shape](that: THAT) = {
    matMul[that.type](that)
  }

  def transpose = {
    shape.requireNumDim(Nat._2).rearrangeBy(Indices & Index.Left(1) & Index.Left(0))
  }

  def `^T` = {
    transpose
  }
}
