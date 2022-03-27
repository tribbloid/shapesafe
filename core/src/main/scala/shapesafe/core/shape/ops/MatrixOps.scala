package shapesafe.core.shape.ops

import shapeless.Nat
import shapesafe.core.shape.Index.LtoR
import shapesafe.core.shape.{Indices, Names, Shape}

trait MatrixOps extends HasShape {

  def matMul[THAT <: Shape](that: THAT) = {
//    val s1 = shape :<<= Names.ij
//    val s2 = that :<<= Names.jk
//
//    s1.einSum(s2) --> Names.ik

    // TODO: above is slight slower, need to optimise

    val outer = (shape >< that) :<<= Names.ijjk
    outer.einSum --> Names.ik
  }

  def mat_*[THAT <: Shape](that: THAT) = {
    matMul[that.type](that)
  }

  def transpose = {
    shape
      .requireNumDim(Nat._2)
      .rearrangeBy(
        Indices & LtoR(1) & LtoR(0)
      )
  }

  def `^T` = {
    transpose
  }
}
