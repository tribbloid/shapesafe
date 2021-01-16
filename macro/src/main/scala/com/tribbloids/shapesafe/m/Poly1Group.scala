package com.tribbloids.shapesafe.m

// TODO: not used, hard to extend?
trait Poly1Group[-IUB, -OUB] {

  trait ~~>[
      -I <: IUB,
      +O <: OUB
  ] {

    def apply(v: I): O
  }

  def apply[I <: IUB, O <: OUB](v: I)(implicit ev: I ~~> O): O = ev(v)
}
