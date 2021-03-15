package org.shapesafe.core.shape

trait IndicesMagnet {

  type AsIndices <: Indices
  def asIndices: AsIndices
}
