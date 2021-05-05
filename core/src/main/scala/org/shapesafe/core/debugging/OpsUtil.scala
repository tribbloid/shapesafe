package org.shapesafe.core.debugging

import shapeless.Witness
import singleton.ops.{+, ITE, IsString}

object OpsUtil {

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

  type Peek[T <: CanPeek] = StrOrRaw[
    T#_Ops
  ]
  object Peek {

    // TODO: brackets?
    type Infix[T1 <: CanPeek, S, T2 <: CanPeek] =
      Peek[T1] + StrOrRaw[S] + Peek[T2]

    type PrefixW1[S, T <: CanPeek] =
      StrOrRaw[S] + Br[Peek[T]]

    type PrefixW2[S, T1 <: CanPeek, T2 <: CanPeek] =
      StrOrRaw[S] + Br[Peek[T1] + ", " + Peek[T2]]
  }

  trait CanRefute {

    type _Refute
  }
  type Refute[T <: CanRefute] = StrOrRaw[
    T#_Refute
  ]

  val shades_+ = Witness(" ░▒▓")

  type Stripe[T] = "\u001b[7m" + T + shades_+.T + "\u001b[0m" + "\n\n"

  object ForArity {

    type TryArity = "\n\n" + Stripe["... when proving arity"]

    type Refute0[SELF <: CanPeek with CanRefute] =
      Refute[SELF] +
        TryArity +
        Peek[SELF]

    type Refute1[SELF <: CanPeek with CanRefute, C1] =
      Refute[SELF] +
        TryArity +
        Peek[SELF] +
        FROM1.T +
        C1

    type Refute2[SELF <: CanPeek with CanRefute, C1, C2] =
      Peek[SELF] +
        TryArity +
        Refute[SELF] +
        FROM2.T +
        C1 +
        "\n\n" +
        C2
  }

  object ForShape {

    type TryShape = "\n\n" + Stripe["... when proving shape"]

    type Refute0[SELF <: CanPeek with CanRefute] =
      Refute[SELF] +
        TryShape +
        Peek[SELF]
  }

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
