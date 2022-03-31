package shapesafe.core.debugging

object Notations extends NotationsLike {

  trait +[A, B] extends HasSymbolLit {
    type _SymbolLit = " + "
  }
  trait -[A, B] extends HasSymbolLit {
    type _SymbolLit = " - "
  }
  trait *[A, B] extends HasSymbolLit {
    type _SymbolLit = " * "
  }
  trait /[A, B] extends HasSymbolLit {
    type _SymbolLit = " / "
  }

  trait ==[A, B] extends HasSymbolLit.HasNegation {
    type _SymbolLit = " == "
    type Negation = =/=[A, B]
  }
  trait =/=[A, B] extends HasSymbolLit.HasNegation {
    type _SymbolLit = " != "
    type Negation = ==[A, B]
  }
  trait >[A, B] extends HasSymbolLit.HasNegation {
    type _SymbolLit = " > "
    type Negation = <=[A, B]
  }
  trait >=[A, B] extends HasSymbolLit.HasNegation {
    type _SymbolLit = " >= "
    type Negation = <[A, B]
  }
  trait <[A, B] extends HasSymbolLit.HasNegation {
    type _SymbolLit = " < "
    type Negation = >=[A, B]
  }
  trait <=[A, B] extends HasSymbolLit.HasNegation {
    type _SymbolLit = " <= "
    type Negation = >[A, B]
  }
}
