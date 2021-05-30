package org.shapesafe.core.debugging

object Expressions extends Expressions_Imp0 {

  trait +[A, B] extends DebugSymbol {
    type _AsOpStr = " + "
  }
  trait -[A, B] extends DebugSymbol {
    type _AsOpStr = " - "
  }
  trait *[A, B] extends DebugSymbol {
    type _AsOpStr = " * "
  }
  trait /[A, B] extends DebugSymbol {
    type _AsOpStr = " / "
  }

  trait ==[A, B] extends DebugSymbol.Require {
    type _AsOpStr = " == "
    type Complement = !=[A, B]
  }
  trait !=[A, B] extends DebugSymbol.Require {
    type _AsOpStr = " != "
    type Complement = ==[A, B]
  }
  trait >[A, B] extends DebugSymbol.Require {
    type _AsOpStr = " > "
    type Complement = <=[A, B]
  }
  trait >=[A, B] extends DebugSymbol.Require {
    type _AsOpStr = " >= "
    type Complement = >[A, B]
  }
  trait <[A, B] extends DebugSymbol.Require {
    type _AsOpStr = " < "
    type Complement = >=[A, B]
  }
  trait <=[A, B] extends DebugSymbol.Require {
    type _AsOpStr = " <= "
    type Complement = >[A, B]
  }
}
