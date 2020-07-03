package edu.umontreal.kotlingrad.shapesafe.m.arity

// doesn't extend T => R intentionally
trait Implies[-T, +R] {
  def transform(v: T): R
}

object Implies {

  implicit def implies[T, R](v: T)(implicit bound: T Implies R): R = bound.transform(v)
}
