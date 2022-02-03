package shapesafe.core.debugging

import singleton.ops.{ITE, IsString}

trait CanRefute extends CanPeek {

  type _Refute

  final type Refute = CanRefute.StrOr_???[
    _Refute
  ]
}

object CanRefute {

  type StrOr_???[T1] = ITE[
    IsString[T1],
    T1,
    "???"
  ]

}
