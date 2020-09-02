package edu.umontreal.kotlingrad.shapesafe.m.arity

import shapeless.Witness

import scala.language.implicitConversions

trait Arity extends Proof {

  override type Out = this.type
  override def out: Out = this
}

object Arity {

  trait Const[S] extends Arity with Proof.Invar[S] {}

  // this makes it impossible to construct directly from Int type
  class FromLiteral[S <: Int](val singleton: S) extends Const[S] {}

  object FromLiteral {

    implicit def summon[S <: Int](implicit w: Witness.Aux[S]): FromLiteral[S] = {
      new FromLiteral[S](w.value)
    }
  }

  def apply(w: Witness.Lt[Int]): FromLiteral[w.T] = {

    FromLiteral.summon[w.T](w) //TODO: IDEA inspection error
  }
}
