package com.tribbloids.shapesafe.m.shape

import shapeless.{::, HList, HNil, LUBConstraint, Witness}

case class NamesView[H <: HList](self: H)(
    implicit bound: LUBConstraint[H, String]
) {

  type HH = H

  def ><(name: Witness.Lt[String]): NamesView[name.T :: H] = NamesView(
    name.value :: self
  )
}

object NamesView {

  type Empty = NamesView[HNil]
  object Empty extends NamesView(HNil: HNil)

  def ><(name: Witness.Lt[String]): NamesView[name.T :: HNil] = Empty >< name
}
