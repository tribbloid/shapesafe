package org.shapesafe.core.shape.ops

import org.shapesafe.core.shape.Shape

trait HasShape {

  type _Shape <: Shape
  def shape: _Shape

  lazy val api = shape.^
}

object HasShape {}
