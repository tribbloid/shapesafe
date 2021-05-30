package org.shapesafe.core.debugging

trait DebugSymbol {
  type _AsOpStr
}

object DebugSymbol {

  trait On1 extends DebugSymbol {

    trait On[A] {}
  }
  trait On2 extends DebugSymbol {

    trait On[A, B] {}
  }

  trait Require extends DebugSymbol {

    type Complement <: Require

//    implicitly[this.type <:< Complement#Complement]
  }
}
