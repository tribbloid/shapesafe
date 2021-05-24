package org.shapesafe.core.arity.binary

import org.shapesafe.core.arity.Utils.Op
import org.shapesafe.core.debugging.Expressions

/**
  * Output is always the MOST SPECIFIC representation of the FIRST argument
  * @tparam ?? operator of 2
  * @tparam SS for compile-time peek
  */
trait Require2[
    ??[X1, X2] <: Op,
    SS[A, B] <: Expressions.DualExpression
] {

  // TODO: this should supersedes AssertEqual
}
