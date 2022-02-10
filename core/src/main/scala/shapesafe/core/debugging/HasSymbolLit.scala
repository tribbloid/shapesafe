package shapesafe.core.debugging

trait HasSymbolLit {
  // Type suffix "Lit" indicates that it can only be a string literal
  type _SymbolLit

  // Type suffix "Txt" indicates that it can be a string literal or
  // a singleton-ops expression that resolves to string type
  final type SymbolTxt = DebugConst.StrOr_???[
    _SymbolLit
  ]
}

object HasSymbolLit {

  trait HasNegation extends HasSymbolLit {

    type Negation <: HasNegation
  }
}
