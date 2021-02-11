package org.shapesafe.core

// TODO: compiler bug?
//  https://stackoverflow.com/questions/65944627/in-scala-how-could-a-covariant-type-parameter-be-an-upper-bound-of-an-abstract
// TODO: merge into shapeless Poly1
trait Poly1Base[IUB, OUB] {

  trait Case[-I <: IUB] {

    type Out <: OUB

    protected[Poly1Base] def apply(v: I): Out

  }

  object Case {

    type Aux[
        I <: IUB,
        O <: OUB
    ] = Case[I] { type Out = O }

    // only use as an implicit type parameter if Output type doesn't depends on O!
    type Lt[
        I <: IUB,
        O <: OUB
    ] = Case[I] { type Out <: O }
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

  def apply[I <: IUB] = new Builder[I]()
  def buildFrom[I <: IUB] = new Builder[I]() // same as `at` in Poly1?

  protected class Builder[I <: IUB]() {

    def to[O <: OUB](fn: I => O): I ==> O = new (I ==> O) {

      final override def apply(v: I): O = fn(v)
    }
  }

  def summon[I <: IUB](
      implicit
      ev: Case[I]
  ): ev.type = ev

  def peek[I <: IUB](v: I)(
      implicit
      ev: Case[I]
  ): ev.type = ev

  def apply[I <: IUB](v: I)(
      implicit
      ev: Case[I]
  ): ev.Out = ev.apply(v)
}
