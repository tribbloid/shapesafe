package edu.umontreal.kotlingrad.shapesafe.arity

trait ArityLike extends Serializable {

  type out <: Arity
  def out: Arity
}
