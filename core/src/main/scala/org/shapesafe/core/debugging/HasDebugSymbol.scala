package org.shapesafe.core.debugging

trait HasDebugSymbol {
  type _DebugSymbol
}

object HasDebugSymbol {

  trait On1 extends HasDebugSymbol {

    trait On[A] {}
  }
  trait On2 extends HasDebugSymbol {

    trait On[A, B] {}
  }

  trait Require extends HasDebugSymbol {

    type Negation <: Require
  }
}
