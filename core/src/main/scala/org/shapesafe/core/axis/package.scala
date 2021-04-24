package org.shapesafe.core

import shapeless.Witness
import singleton.ops.XString

package object axis {

  final val noName: XString = ""
  type NoName = noName.type
  val NoNameW: Witness.Aux[NoName] = Witness[NoName]
}
