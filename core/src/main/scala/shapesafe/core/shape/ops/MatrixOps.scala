package shapesafe.core.shape.ops

import shapeless.Nat
import shapesafe.core.shape.{Index, Indices, Names, Shape}

trait MatrixOps extends HasShape {

  def matMul[THAT <: Shape](that: THAT) = {
    val s1 = shape :<<= Names.ij
    val s2 = that :<<= Names.jk

    s1.einSum(s2) --> Names.ik
  }

  def mat_*[THAT <: Shape](that: THAT) = {
    matMul[that.type](that)
  }

  def transpose = {
    shape.requireNumDim(Nat._2).rearrangeBy(Indices & Index.LtoR(1) & Index.LtoR(0))
  }

  def `^T` = {
    transpose
  }
}
