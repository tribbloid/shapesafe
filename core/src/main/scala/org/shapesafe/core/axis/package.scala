package org.shapesafe.core

import shapeless.Witness

package object axis {

  val noName: String with Singleton = ""
  type NoName = noName.type
  val NoNameW: Witness.Aux[NoName] = Witness[NoName]
}
