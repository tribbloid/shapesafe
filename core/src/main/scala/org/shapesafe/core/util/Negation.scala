package org.shapesafe.core.util

/**
  * Negation symbol, see the fancy answer at:
  * https://stackoverflow.com/questions/6909053/enforce-type-difference/6944070#6944070
  * see test for examples
  */
class Negation[T] {

  type Of = T
}

object Negation {

  implicit def summon[T]: Negation[T] = new Negation[T]

  implicit def deliberateConflict1[T](
      implicit
      v: T
  ): Negation[T] = null

  implicit def deliberateConflict2[T](
      implicit
      v: T
  ): Negation[T] = null
}
