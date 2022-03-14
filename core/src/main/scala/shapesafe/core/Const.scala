package shapesafe.core

import shapeless.Witness

object Const {

  type NoName = ""
  lazy val NoNameW: Witness.Aux[NoName] = Witness[NoName]

  object True // TODO: should already exists in scala library
  type True = True.type

  implicit def _summonTrue: True = True
}
