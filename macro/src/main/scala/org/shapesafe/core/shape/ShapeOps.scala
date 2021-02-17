package org.shapesafe.core.shape

import org.shapesafe.core.shape.unary.Rename

case class ShapeOps[SELF <: Shape](val self: SELF) {

  /**
    * assign new names
    * @param newNames a tuple of names
    */
  def |<<-[N <: Names](newNames: N): Rename[SELF, N] = {

    Rename[SELF, N](self, newNames)
  }
}
