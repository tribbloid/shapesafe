package org.shapesafe.core.debugging

import org.shapesafe.core.debugging.DebugUtil._
import singleton.ops.+

object OpStrs {

  type OpStr[T <: CanPeek] = StrOr_???[
    T#_AsOpStr
  ]

  // TODO: brackets?
  type Infix[T1 <: CanPeek, S, T2 <: CanPeek] =
    OpStr[T1] + StrOr_???[S] + OpStr[T2]

  type PrefixW1[S, T <: CanPeek] =
    StrOr_???[S] + Br[OpStr[T]]

  type PrefixW2[S, T1 <: CanPeek, T2 <: CanPeek] =
    StrOr_???[S] + Br[OpStr[T1] + ", " + OpStr[T2]]

}
