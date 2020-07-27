package edu.umontreal.kotlingrad.shapesafe.m

import edu.umontreal.kotlingrad.shapesafe.m.arity.Proven

import scala.language.implicitConversions

// doesn't extend T => R intentionally
trait ~~>[-T, +R] {
  def apply(v: T): R
}

object ~~> {

  implicit def summon[T, R](v: T)(implicit bound: T ~~> R): R = bound.apply(v)

  implicit def trivial[T]: T ~~> T = identity
}
