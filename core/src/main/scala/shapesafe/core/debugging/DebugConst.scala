package shapesafe.core.debugging

import singleton.ops.{+, ITE, IsString}

object DebugConst {

  type StrOr_???[T1] = ITE[
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

  final val SPACE4 = "    "
  final val shades_+ = " ░▒▓"
  final val shades_- = "▓▒░ "

  type Stripe[T] = SPACE4.type + shades_-.type + T + shades_+.type + "\n\n"

  // TODO: add another instance that shows reasoning process?

//  val LF = ("\n")

  val EMPTY = ""

  final val REFUTE =
    """¯\_(ツ)_/¯ """
  final val PEEK =
    """      """
  final val EQUIV =
    """  :=  """

  type EquivLF = "\n" + EQUIV.type

  type CannotEval = Stripe["cannot evaluate"]

  final val ILLEGAL_OP = """(Illegal Operation) """

  final val IMPOSSIBLE = "IMPOSSIBLE!"

  type Br[T] = "(" + T + ")"

  final val FROM1 = "\n\n... with 1 prior >\n\n"
  final val FROM2 = "\n\n... with 2 priors >\n\n"

  val INTERNAL_ERROR = "[INTERNAL ERROR] Please submit a bug report using the following expression ..."
}
