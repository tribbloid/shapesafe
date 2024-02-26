package shapesafe.core.debugging

trait CanRefute extends HasNotation {

  type _RefuteTxt

  final type RefuteTxt = DebugConst.StrOr_???[
    _RefuteTxt
  ]
}

object CanRefute {}
