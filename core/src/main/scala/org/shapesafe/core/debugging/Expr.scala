package org.shapesafe.core.debugging

object Expr extends ExprLike {

  trait +[A, B] extends HasLiteral {
    type Lit = " + "
  }
  trait -[A, B] extends HasLiteral {
    type Lit = " - "
  }
  trait *[A, B] extends HasLiteral {
    type Lit = " * "
  }
  trait /[A, B] extends HasLiteral {
    type Lit = " / "
  }
  trait ==[A, B] extends HasLiteral {
    type Lit = " == "
  }
}
