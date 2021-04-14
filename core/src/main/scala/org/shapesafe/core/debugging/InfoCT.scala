package org.shapesafe.core.debugging

import org.shapesafe.m.TypeToLiteral
import shapeless.Witness
import singleton.ops.{+, ITE, IsString, RequireMsg}

object InfoCT {

  type StrOrRaw[T1] = ITE[
    IsString[T1],
    T1,
    "???"
  ]

  // TODO: is it possible and better?
  //  also need time for https://github.com/fthomas/singleton-ops/issues/182
  TypeToLiteral
//  type StrOrRaw[T1] = ITE[
//    IsString[T1],
//    T1,
//    TypeToLiteral.Type.Case[T1]
//  ]

  trait CanPeek {

    protected[InfoCT] type _Peek
  }
  type Peek[T <: CanPeek] = StrOrRaw[
    T#_Peek
  ]

  trait CanRefute {

    protected[InfoCT] type _Refute
  }
  type Refute[T <: CanRefute] = StrOrRaw[
    T#_Refute
  ]

  // TODO: add another instance that shows reasoning process?

  type ReportMsg[T] = RequireMsg[FALSE.T, T] // always fail, force the message to be displayed at compile time

  type PeekMsg[T] = ReportMsg[InfoCT.peek.T + T]

  val LF = Witness("\n")

  val EMPTY = Witness("")

  val FALSE = Witness(false)

  val YIELD = Witness(" := ")

  val noCanDo = Witness("""¯\_(ツ)_/¯  """)

  val nonExisting = Witness(""" ∄ """)

  val peek = Witness("> ")

  val impossible = Witness("IMPOSSIBLE!")

  type Br[T] = "(" + T + ")"
}
