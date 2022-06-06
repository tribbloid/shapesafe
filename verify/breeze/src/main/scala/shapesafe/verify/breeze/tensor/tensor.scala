package shapesafe.verify.breeze

import breeze.linalg

package object tensor {

  type Vec[T] = linalg.Vector[T]
  val Vec = linalg.Vector
}
