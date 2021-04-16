package org.shapesafe.core.debugging

import org.shapesafe.m.TypeToLiteral
import shapeless.Witness
import singleton.ops.{+, ITE, IsString}

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

    type _Peek
  }

  type Peek[T <: CanPeek] = StrOrRaw[
    T#_Peek
  ]

//  type Peek0[T <: CanPeek] = Peek[T] +
//    "\n"

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

    type _Refute
  }
  type Refute[T <: CanRefute] = StrOrRaw[
    T#_Refute
  ]

  object ForArity {

    val TRY_ARITY = Witness("\n\n| trying to prove arity >\n\n")

    type Refute0[SELF <: CanPeek with CanRefute] =
      Refute[SELF] +
        TRY_ARITY.T +
        Peek[SELF]

    type Refute1[SELF <: CanPeek with CanRefute, C1] =
      Refute[SELF] +
        TRY_ARITY.T +
        Peek[SELF] +
        FROM1.T +
        C1

    type Refute2[SELF <: CanPeek with CanRefute, C1, C2] =
      Peek[SELF] +
        TRY_ARITY.T +
        Refute[SELF] +
        FROM2.T +
        C1 +
        "\n\n" +
        C2
  }

  object ForShape {

    val TRY_SHAPE = Witness("\n\n| trying to prove shape >\n\n")

    type Refute0[SELF <: CanPeek with CanRefute] =
      Refute[SELF] +
        TRY_SHAPE.T +
        Peek[SELF]
  }

  // TODO: add another instance that shows reasoning process?

//  val LF = Witness("\n")

  val EMPTY = Witness("")

  val FALSE = Witness(false)

  val REFUTE =
    Witness("""¯\_(ツ)_/¯ """)
  val PEEK =
    Witness("""|>    """)
  val ENTAILS =
    Witness("""  :=  """)

  type EntailsLF = "\n" + ENTAILS.T

  val nonExisting = Witness(""" ∄ """)

  val impossible = Witness("IMPOSSIBLE!")

  type Br[T] = "(" + T + ")"

  val FROM1 = Witness("\n\n| with 1 prior >\n\n")
  val FROM2 = Witness("\n\n| with 2 priors >\n\n")
}
