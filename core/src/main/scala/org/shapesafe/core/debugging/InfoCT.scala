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
  object Peek {

    // TODO: brackets?
    type InfixW[T1 <: CanPeek, S, T2 <: CanPeek] =
      Peek[T1] + StrOrRaw[S] + Peek[T2]

    type PrefixW1[S, T <: CanPeek] =
      StrOrRaw[S] + Br[Peek[T]]

    type PrefixW2[S, T1 <: CanPeek, T2 <: CanPeek] =
      StrOrRaw[S] + Br[Peek[T1] + ", " + Peek[T2]]
  }

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

  val apply = Witness("")

  type Br[T] = "(" + T + ")"
}
