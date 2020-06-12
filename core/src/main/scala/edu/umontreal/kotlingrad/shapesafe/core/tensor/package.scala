package edu.umontreal.kotlingrad.shapesafe.core

import breeze.linalg
import edu.umontreal.kotlingrad.shapesafe.m.arity.Operand

package object tensor {

  type Shape = Operand

  type Vec[T] = linalg.Vector[T]
  val Vec = linalg.Vector
}
