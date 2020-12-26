package com.tribbloids.shapesafe.core

import breeze.linalg
import com.tribbloids.shapesafe.m.arity.Operand

package object tensor {

  type Shape = Operand

  type Vec[T] = linalg.Vector[T]
  val Vec = linalg.Vector
}
