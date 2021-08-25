package org.shapesafe.core

import shapeless.Poly1

import scala.annotation.implicitNotFound

// TODO: compiler bug?
//  https://stackoverflow.com/questions/65944627/in-scala-how-could-a-covariant-type-parameter-be-an-upper-bound-of-an-abstract
// TODO: merge into shapeless Poly1
trait Poly1Base[IUB, OUB] {

  final type _IUB = IUB
  final type _OUB = OUB

  // TODO: how to override it in subclasses?
  @implicitNotFound(
    "[MISSING]:\n${I}\n    ==>\n???\n"
  )
  trait Case[-I <: IUB] {

    type Out <: OUB

    def apply(v: I): Out
  }

  object Case {

    type Aux[
        I <: IUB,
        O <: OUB
    ] = Case[I] {
      type Out = O
    }

    // only use as an implicit type parameter if Output type doesn't depends on O!
    type Lt[
        I <: IUB,
        O <: OUB
    ] = Case[I] {
      type Out <: O
    }
  }

  @implicitNotFound(
    "[MISSING]:\n${I}\n    ==>\n${O}\n"
  )
  class ==>[
      -I <: IUB,
      O <: OUB
  ](val toOut: I => O)
      extends Case[I] {

    final type Out = O

    override def apply(v: I): O = toOut(v)
  }

  def forAll[I <: IUB] = new ForAll[I]() // same as `at` in Poly1?

  protected class ForAll[I <: IUB]() {

    def ==>[O <: OUB](fn: I => O): I ==> O = new (I ==> O)(fn)
  }

  def summon[I <: IUB](
      implicit
      _case: Case[I]
  ): _case.type = _case

  def summonFor[I <: IUB](v: I)(
      implicit
      _case: Case[I]
  ): _case.type = _case

  def apply[I <: IUB](v: I)(
      implicit
      _case: Case[I]
  ): _case.Out = _case.apply(v)

  object AsShapelessPoly1 extends Poly1 {

    val outer: Poly1Base[IUB, OUB] = Poly1Base.this

    implicit def delegate[I <: IUB, O <: OUB](
        implicit
        from: I ==> O
    ): Case.Aux[I, O] = at[I].apply { ii =>
      from.apply(ii)
    }
  }
}

object Poly1Base {}
