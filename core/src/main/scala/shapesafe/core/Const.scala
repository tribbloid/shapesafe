package shapesafe.core

object Const {

  type NoName = ""
  lazy val NoNameW: "" = ""

  object True // TODO: should already exists in scala library
  type True = True.type

  implicit def _summonTrue: True = True
}
