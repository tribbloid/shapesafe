package org.shapesafe.core.debugging

object Expressions extends Expressions_Imp0 {

  trait +[A, B] extends DebugSymbol {
    type _AsStr = " + "
  }
  trait -[A, B] extends DebugSymbol {
    type _AsStr = " - "
  }
  trait *[A, B] extends DebugSymbol {
    type _AsStr = " * "
  }
  trait /[A, B] extends DebugSymbol {
    type _AsStr = " / "
  }

  trait ==[A, B] extends DebugSymbol.Require {
    type _AsStr = " == "
    type Complement = !=[A, B]
  }
  trait !=[A, B] extends DebugSymbol.Require {
    type _AsStr = " != "
    type Complement = ==[A, B]
  }
  trait >[A, B] extends DebugSymbol.Require {
    type _AsStr = " > "
    type Complement = <=[A, B]
  }
  trait >=[A, B] extends DebugSymbol.Require {
    type _AsStr = " >= "
    type Complement = >[A, B]
  }
  trait <[A, B] extends DebugSymbol.Require {
    type _AsStr = " < "
    type Complement = >=[A, B]
  }
  trait <=[A, B] extends DebugSymbol.Require {
    type _AsStr = " <= "
    type Complement = >[A, B]
  }
}
