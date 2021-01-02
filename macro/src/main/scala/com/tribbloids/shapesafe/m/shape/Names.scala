package com.tribbloids.shapesafe.m.shape

import shapeless.{::, HList, HNil, LUBConstraint, Witness}

case class Names[H1 <: HList](self: H1)(
    implicit bound: LUBConstraint[H1, String]
) {

  type HH = H1

  def |(name: Witness.Lt[String]): Names[name.T :: H1] = Names(
    name.value :: self
  )
}

object Names {

  type Empty = Names[HNil]
  object Empty extends Names(HNil: HNil)

  def ~(name: Witness.Lt[String]): Names[name.T :: HNil] = Empty | name
}
