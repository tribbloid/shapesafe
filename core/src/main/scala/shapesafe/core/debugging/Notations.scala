package shapesafe.core.debugging

object Notations extends NotationsLike {

  trait +[A, B] extends HasSymbolTxt {
    type _SymbolTxt = " + "
  }
  trait -[A, B] extends HasSymbolTxt {
    type _SymbolTxt = " - "
  }
  trait *[A, B] extends HasSymbolTxt {
    type _SymbolTxt = " * "
  }
  trait /[A, B] extends HasSymbolTxt {
    type _SymbolTxt = " / "
  }

  trait ==[A, B] extends HasSymbolTxt.HasNegation {
    type _SymbolTxt = " == "
    type Negation = =/=[A, B]
  }
  trait =/=[A, B] extends HasSymbolTxt.HasNegation {
    type _SymbolTxt = " != "
    type Negation = ==[A, B]
  }
  trait >[A, B] extends HasSymbolTxt.HasNegation {
    type _SymbolTxt = " > "
    type Negation = <=[A, B]
  }
  trait >=[A, B] extends HasSymbolTxt.HasNegation {
    type _SymbolTxt = " >= "
    type Negation = >[A, B]
  }
  trait <[A, B] extends HasSymbolTxt.HasNegation {
    type _SymbolTxt = " < "
    type Negation = >=[A, B]
  }
  trait <=[A, B] extends HasSymbolTxt.HasNegation {
    type _SymbolTxt = " <= "
    type Negation = >[A, B]
  }
}
