package shapesafe.breeze

import breeze.linalg

package object tensor {

  type Vec[T] = linalg.Vector[T]
  val Vec = linalg.Vector
}
