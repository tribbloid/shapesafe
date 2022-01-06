package shapesafe.core.debugging

object Expressions extends ExpressionsLike {

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
