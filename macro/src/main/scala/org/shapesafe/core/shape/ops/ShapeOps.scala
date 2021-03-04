package org.shapesafe.core.shape.ops

import org.shapesafe.core.arity.ops.ArityOps
import org.shapesafe.core.shape.binary.OuterProduct
import org.shapesafe.core.shape.unary.{CheckDistinct, CheckEinSum, Reorder, WithNames}
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
  ): OuterProduct[SELF, THAT] = {

    OuterProduct(self, that)
  }

  def outer[THAT <: Shape](
      that: THAT
  ): OuterProduct[SELF, THAT] = {

    ><(that)
  }

  def einSum: CheckEinSum[SELF] = CheckEinSum(self)

  def contract[N <: Names](names: N): Reorder[CheckEinSum[SELF], N] = {

    einSum.->(names)
  }

  def flattenWith[THAT <: Shape](
      infix: ArityOps.Infix,
      that: THAT
  ): infix.SquashByName[OuterProduct[SELF, THAT]] = {

    val outered: OuterProduct[SELF, THAT] = self.outer(that)
    infix.SquashByName.On(outered)
  }

  def flatten(
      infix: ArityOps.Infix
  ): infix.SquashByName[SELF] = {

    infix.SquashByName.On(self)
  }

  def transpose[N <: Names](names: N): Reorder[CheckDistinct[SELF], N] = {

    Reorder(CheckDistinct(self), names)
  }
}
