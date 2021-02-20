package org.shapesafe.core.shape.ops

import org.shapesafe.core.shape.binary.Direct
import org.shapesafe.core.shape.unary.{CheckEinSum, Reorder, WithNames}
import org.shapesafe.core.shape.{Names, Shape}

class ShapeOps[SELF <: Shape](val self: SELF) {

  /**
    * assign new names
    * @param newNames a tuple of names
    */
  def |<<-[N <: Names](newNames: N): WithNames[SELF, N] = {

    WithNames[SELF, N](self, newNames)
  }

  def ><[THAT <: Shape](
      that: THAT
  ): Direct[SELF, THAT] = {

    Direct(self, that)
  }

  def direct[THAT <: Shape](
      that: THAT
  ): Direct[SELF, THAT] = {

    ><(that)
  }

  def einSum: CheckEinSum[SELF] = CheckEinSum(self)

  def contract[N <: Names](names: N): Reorder[CheckEinSum[SELF], N] = {

    einSum.->(names)
  }

}
