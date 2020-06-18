package edu.umontreal.kotlingrad.shapesafe.`macro`.arity

// for "Expression"
trait Expr extends Proof {}

object Expr {

  // for "Expression"
  abstract class Out_=[O <: Arity]()(implicit final val out: O) extends Expr {

    final type Out = O
  }
}
