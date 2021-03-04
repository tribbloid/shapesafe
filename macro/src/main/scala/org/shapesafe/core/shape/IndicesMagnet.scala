package org.shapesafe.core.shape

trait IndicesMagnet {

  type Canonical <: Indices
  def canonical: Canonical
}
