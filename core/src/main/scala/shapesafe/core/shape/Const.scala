package shapesafe.core.shape

import shapeless.Witness

object Const {

  type NoName = ""
  lazy val NoNameW: Witness.Aux[NoName] = Witness[NoName]

  val i = Names("i")
  val ij = Names("i", "j")
  val jk = Names("j", "k")
  val ik = Names("i", "k")

  val shape3 = Shape(3)
}
