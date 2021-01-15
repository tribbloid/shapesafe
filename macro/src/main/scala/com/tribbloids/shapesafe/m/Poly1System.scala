package com.tribbloids.shapesafe.m

// TODO: not used, hard to extend
trait Poly1System[-IUB, +OLB] {

  trait Fn[
      -IN <: IUB,
      +OUT >: OLB
  ] {}
}
