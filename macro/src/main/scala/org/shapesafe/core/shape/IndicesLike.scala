package org.shapesafe.core.shape

trait IndicesLike {

  type Canonical <: Indices
  def canonical: Canonical
}
