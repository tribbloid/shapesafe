package org.shapesafe.core.debugging

object Expressions extends ExpressionsLike {

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
