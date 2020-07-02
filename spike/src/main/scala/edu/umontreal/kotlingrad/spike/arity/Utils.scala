package edu.umontreal.kotlingrad.spike.arity

object Utils {

  case class Summoner[R]() {

    def summon[T](v: T)(implicit ev: T => R): R = ev(v)
  }
}
