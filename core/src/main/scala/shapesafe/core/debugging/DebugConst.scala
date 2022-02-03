package shapesafe.core.debugging

import shapeless.Witness
import singleton.ops.+

object DebugConst {

  // TODO: is it possible and better?
  //  also need time for https://github.com/fthomas/singleton-ops/issues/182
  //  type StrOrRaw[T1] = ITE[
  //    IsString[T1],
  //    T1,
  //    TypeToLiteral.Type.Case[T1]
  //  ]

  val shades_+ = Witness(" ░▒▓")

  type Stripe[T] = "\u001b[7m" + T + shades_+.T + "\u001b[0m" + "\n\n"

  // TODO: add another instance that shows reasoning process?

//  val LF = Witness("\n")

  val EMPTY = Witness("")

  val REFUTE =
    Witness("""¯\_(ツ)_/¯ """)
  val PEEK =
    Witness("""      """)
  val EQUIV =
    Witness("""  :=  """)

  type EquivLF = "\n\n" + EQUIV.T

  type CannotEval = Stripe["cannot evaluate"]

  val ILLEGAL_OP = Witness("""(Illegal Operation) """)

  val IMPOSSIBLE = Witness("IMPOSSIBLE!")

  type Br[T] = "(" + T + ")"

  val FROM1 = Witness("\n\n... with 1 prior >\n\n")
  val FROM2 = Witness("\n\n... with 2 priors >\n\n")

  val INTERNAL_ERROR = "[INTERNAL ERROR] Please submit a bug report using the following expression ..."
}
