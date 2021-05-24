package org.shapesafe.core.debugging

import org.shapesafe.core.debugging.DebugUtil._
import singleton.ops.+

object OpStrs {

  type OpStr[T <: CanPeek] = StrOrRaw[
    T#_AsStr
  ]

  // TODO: brackets?
  type Infix[T1 <: CanPeek, S, T2 <: CanPeek] =
    OpStr[T1] + StrOrRaw[S] + OpStr[T2]

  type PrefixW1[S, T <: CanPeek] =
    StrOrRaw[S] + Br[OpStr[T]]

  type PrefixW2[S, T1 <: CanPeek, T2 <: CanPeek] =
    StrOrRaw[S] + Br[OpStr[T1] + ", " + OpStr[T2]]

  object ForArity {

    type TryArity = "\n\n" + Stripe["... when proving arity"]

    type Refute0[SELF <: CanPeek with CanRefute] =
      Refute[SELF] +
        TryArity +
        OpStr[SELF]

    //    type Refute1[SELF <: CanPeek with CanRefute, C1] =
    //      Refute[SELF] +
    //        TryArity +
    //        OpStr[SELF] +
    //        FROM1.T +
    //        C1
    //
    //    type Refute2[SELF <: CanPeek with CanRefute, C1, C2] =
    //      OpStr[SELF] +
    //        TryArity +
    //        Refute[SELF] +
    //        FROM2.T +
    //        C1 +
    //        "\n\n" +
    //        C2
  }

  object ForShape {

    type TryShape = "\n\n" + Stripe["... when proving shape"]

    type Refute0[SELF <: CanPeek with CanRefute] =
      Refute[SELF] +
        TryShape +
        OpStr[SELF]
  }
}
