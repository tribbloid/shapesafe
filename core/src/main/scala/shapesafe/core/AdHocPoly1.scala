package shapesafe.core

import shapeless.Poly1

import scala.annotation.implicitNotFound

// TODO: compiler bug?
//  https://stackoverflow.com/questions/65944627/in-scala-how-could-a-covariant-type-parameter-be-an-upper-bound-of-an-abstract

/**
  * Different from dotty's polymorphic function kind, which only supports parametric polymorphic
  */
trait AdHocPoly1 {

  trait CaseFrom[-I] {

    type Out

    def apply(v: I): Out
  }

  class =>>[
      -I,
      O
  ](val toOut: I => O)
      extends CaseFrom[I] {

    final type Out = O

    override def apply(v: I): O = toOut(v)
  }

  object Auxs {

    final type =>>[
        I,
        O
    ] = CaseFrom[I] {
      type Out = O
    }

    final type =>>:<[
        I,
        O
    ] = CaseFrom[I] {
      type Out <: O
    }
  }

  object AuxsWithNotFoundMsg {

    @implicitNotFound(
      "${I}\t =/=>> \t??? <: ${OUB}"
    )
    final type Case[I] = AdHocPoly1.this.CaseFrom[I]

    @implicitNotFound(
      "${I}\t =/=>> \t${O}"
    )
    final type =>>[
        I,
        O
    ] = Case[I] {
      type Out = O
    }

    // only use as an implicit type parameter if Output type doesn't depends on O!
    @implicitNotFound(
      "${I}\t =/=>> \t??? <: ${O}"
    )
    final type =>>:<[
        I,
        O
    ] = Case[I] {
      type Out <: O
    }
  }

  def forAll[I] = new ForAll[I]() // same as `at` in Poly1?

  protected class ForAll[I]() {

    def =>>[O](fn: I => O): I =>> O = new (I =>> O)(fn)
  }

  def summon[I](
      implicit
      _case: CaseFrom[I]
  ): _case.type = _case

  def summonFor[I](v: I)(
      implicit
      _case: CaseFrom[I]
  ): _case.type = _case

  def apply[I](v: I)(
      implicit
      _case: CaseFrom[I]
  ): _case.Out = _case.apply(v)

  object AsShapelessPoly1 extends Poly1 {

    val outer: AdHocPoly1 = AdHocPoly1.this

    implicit def delegate[I, O](
        implicit
        from: I =>> O
    ): Case.Aux[I, O] = at[I].apply { ii =>
      from.apply(ii)
    }
  }
}

object AdHocPoly1 {}
