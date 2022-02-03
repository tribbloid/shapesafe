package shapesafe.core.debugging

trait HasSymbolTxt {

  type _SymbolTxt

  final type SymbolTxt = DebugConst.StrOr_???[
    _SymbolTxt
  ]
}

object HasSymbolTxt {

  trait HasNegation extends HasSymbolTxt {

    type Negation <: HasNegation
  }
}
