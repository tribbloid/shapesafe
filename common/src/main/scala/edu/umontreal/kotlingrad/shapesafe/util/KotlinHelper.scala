package edu.umontreal.kotlingrad.shapesafe.util

import edu.umontreal.kotlingrad.shapesafe.arity.Arity
import shapeless.Witness
import shapeless.Witness.Lt

object KotlinHelper {

  def witnessOf[T](v: T): Lt[T] = Witness.apply(v)

  class Arity1 extends Arity.FromInt(Witness(1))
  class Arity2 extends Arity.FromInt(Witness(2))
  class Arity3 extends Arity.FromInt(Witness(3))
  class Arity4 extends Arity.FromInt(Witness(5))
}
