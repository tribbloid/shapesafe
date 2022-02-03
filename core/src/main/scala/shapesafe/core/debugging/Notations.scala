package shapesafe.core.debugging

object Notations extends NotationsLike {

  trait +[A, B] extends HasDebugSymbol {
    type _DebugSymbol = " + "
  }
  trait -[A, B] extends HasDebugSymbol {
    type _DebugSymbol = " - "
  }
  trait *[A, B] extends HasDebugSymbol {
    type _DebugSymbol = " * "
  }
  trait /[A, B] extends HasDebugSymbol {
    type _DebugSymbol = " / "
  }

  trait ==[A, B] extends HasDebugSymbol.Require {
    type _DebugSymbol = " == "
    type Negation = !=[A, B]
  }
  trait !=[A, B] extends HasDebugSymbol.Require {
    type _DebugSymbol = " != "
    type Negation = ==[A, B]
  }
  trait >[A, B] extends HasDebugSymbol.Require {
    type _DebugSymbol = " > "
    type Negation = <=[A, B]
  }
  trait >=[A, B] extends HasDebugSymbol.Require {
    type _DebugSymbol = " >= "
    type Negation = >[A, B]
  }
  trait <[A, B] extends HasDebugSymbol.Require {
    type _DebugSymbol = " < "
    type Negation = >=[A, B]
  }
  trait <=[A, B] extends HasDebugSymbol.Require {
    type _DebugSymbol = " <= "
    type Negation = >[A, B]
  }
}
