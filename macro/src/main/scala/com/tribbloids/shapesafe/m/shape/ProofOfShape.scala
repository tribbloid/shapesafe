package com.tribbloids.shapesafe.m.shape

import com.tribbloids.shapesafe.m.Proof

trait ProofOfShape extends Proof {

  override type Out <: Shape
}
