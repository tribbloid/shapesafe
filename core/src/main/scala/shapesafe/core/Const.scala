package shapesafe.core

import shapeless.Witness
import shapesafe.core.shape.{Names, Shape}

object Const {

  type NoName = ""
  lazy val NoNameW: Witness.Aux[NoName] = Witness[NoName]

  val i = Names("i")
  val ij = Names("i", "j")
  val jk = Names("j", "k")
  val ik = Names("i", "k")

  val shape3 = Shape(3)

  object True // TODO: should already exists in scala library
  type True = True.type

  implicit def _summonTrue: True = True
}
