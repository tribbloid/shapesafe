package org.shapesafe.core.debugging

import shapeless.Witness
import singleton.ops.{+, ITE, IsString}

object DebuggingUtil {

  type StrOrRaw[T1] = ITE[
    IsString[T1],
    T1,
    "???"
  ]

  // TODO: is it possible and better?
  //  also need time for https://github.com/fthomas/singleton-ops/issues/182

//  type StrOrRaw[T1] = ITE[
//    IsString[T1],
//    T1,
//    TypeToLiteral.Type.Case[T1]
//  ]

  trait CanRefute {

    type _Refute
  }
  type Refute[T <: CanRefute] = StrOrRaw[
    T#_Refute
  ]

  val shades_+ = Witness(" ░▒▓")

  type Stripe[T] = "\u001b[7m" + T + shades_+.T + "\u001b[0m" + "\n\n"

  // TODO: add another instance that shows reasoning process?

//  val LF = Witness("\n")

  val EMPTY = Witness("")

  val REFUTE =
    Witness("""¯\_(ツ)_/¯ """)
  val PEEK =
    Witness("""      """)
  val ENTAILS =
    Witness("""  :=  """)

  type EntailsLF = "\n" + ENTAILS.T

  type CannotEval = Stripe["cannot evaluate"]

  val nonExisting = Witness(""" Undefined """)

  val impossible = Witness("IMPOSSIBLE!")

  type Br[T] = "(" + T + ")"

  val FROM1 = Witness("\n\n... with 1 prior >\n\n")
  val FROM2 = Witness("\n\n... with 2 priors >\n\n")
}
