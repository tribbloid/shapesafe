package org.shapesafe.core.debugging

object Expressions extends Expressions_Imp0 {

  trait +[A, B] extends DualExpression {
    type _OpStr = " + "
  }
  trait -[A, B] extends DualExpression {
    type _OpStr = " - "
  }
  trait *[A, B] extends DualExpression {
    type _OpStr = " * "
  }
  trait /[A, B] extends DualExpression {
    type _OpStr = " / "
  }
  trait ==[A, B] extends DualExpression {
    type _OpStr = " == "
  }
}
