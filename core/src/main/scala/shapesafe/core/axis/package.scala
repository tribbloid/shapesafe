package shapesafe.core

import shapeless.Witness

package object axis {

  final val noName: XString = ""
  type NoName = noName.type
  val NoNameW: Witness.Aux[NoName] = Witness[NoName]
}
