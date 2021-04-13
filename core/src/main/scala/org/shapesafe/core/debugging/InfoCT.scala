package org.shapesafe.core.debugging

import shapeless.Witness
import singleton.ops.{ITE, IsString, RequireMsg}

object InfoCT {

  type StrOr_?[T1] = ITE[
    IsString[T1],
    T1,
    "???"
  ]

  trait CanPeek {

    type _Peek

    final type Peek = StrOr_?[_Peek]
  }

  trait CanRefute {}

//  type Lt[I] = InfoCT { type _Info <: I }

//  class Direct[_INFO]() extends InfoCT {
//
//    final override type _Info = _INFO
//  }
//
//  def direct[T]: Direct[T] = new Direct[T]()
//  def direct(w: Witness.Lt[String]): Direct[w.T] = direct[w.T]

  // TODO: add another instance that shows reasoning process?

  type Fail[T] = RequireMsg[FALSE.T, T] // always fail, force the message to be displayed at compile time

  val LF = Witness("\n")

  val EMPTY = Witness("")

  val FALSE = Witness(false)

  val noCanDo = Witness("""¯\_(ツ)_/¯  """)

  val nonExisting = Witness(""" ∄ """)

  val peek = Witness("͡o _ ͡o  ")

  val impossible = Witness("IMPOSSIBLE!")
}
