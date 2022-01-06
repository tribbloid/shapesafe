package shapesafe.core.debugging

trait HasDebugSymbol {
  type _DebugSymbol
}

object HasDebugSymbol {

  trait ExprOn1 extends HasDebugSymbol {

    trait On[A] {}

    type Apply[AA <: CanPeek] = On[AA#Expr]
  }
  trait ExprOn2 extends HasDebugSymbol {

    trait On[A, B] {}

    type Apply[AA <: CanPeek, BB <: CanPeek] = On[AA#Expr, BB#Expr]
  }

  trait Require extends HasDebugSymbol {

    type Negation <: Require
  }
}
