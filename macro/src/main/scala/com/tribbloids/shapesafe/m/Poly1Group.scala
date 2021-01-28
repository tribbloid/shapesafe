package com.tribbloids.shapesafe.m

// TODO: hard to extend?
// TODO: compiler bug?
//  https://stackoverflow.com/questions/65944627/in-scala-how-could-a-covariant-type-parameter-be-an-upper-bound-of-an-abstract
trait Poly1Group[-IUB, OUB] {

  trait Case[-I <: IUB] {

    type Out <: OUB

    def apply(v: I): Out

  }

  trait ==>[
      -I <: IUB,
      O <: OUB
  ] extends Case[I] {

    final type Out = O
  }

//  case class FromFn[
//      -I <: IUB,
//      O <: OUB
//  ](fn: I => O)
//      extends (I ==> O) {
//
//    final override def apply(v: I): O = fn(v)
//  }

  def apply[I <: IUB] = Builder[I]()

  case class Builder[I <: IUB]() {

    def build[O <: OUB](fn: I => O) = new ==>[I, O] {

      final override def apply(v: I): O = fn(v)
    }
  }

  def summon[I <: IUB](implicit ev: Case[I]): ev.type = ev

  def peek[I <: IUB](v: I)(implicit ev: Case[I]): ev.type = ev

  def apply[I <: IUB](v: I)(implicit ev: Case[I]): ev.Out = ev(v)
}
