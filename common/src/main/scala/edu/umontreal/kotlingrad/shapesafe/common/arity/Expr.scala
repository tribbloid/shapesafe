package edu.umontreal.kotlingrad.shapesafe.common.arity

// for "Expression"
trait Expr extends ArityLike {}

object Expr {

  // for "Expression"
  abstract class Out[O <: Arity]()(implicit final val Out: O) extends ArityLike {

    type Out = O
  }
}
