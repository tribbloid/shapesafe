package org.shapesafe.core.util

import shapeless.Witness
import singleton.ops.RequireMsg

object CompileMsgs {
  trait Base

  trait ToString extends Base {

    type _ToString

    //TODO: final override val toString = ...
  }

  trait NotFound extends Base {

    type _NotFoundMsg
  }

  type Fail[T] = RequireMsg[FALSE.T, T] // always fail, force the message to be displayed at compile time

  val LF = Witness("\n")

  val EMPTY = Witness("")

  val FALSE = Witness(false)

  val prefix = Witness(""" ¯\_(ツ)_/¯ """)

}
