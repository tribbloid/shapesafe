package org.shapesafe.core

import shapeless.{DepFn1, DepFn2, Poly1}

// TODO: compiler bug?
//  https://stackoverflow.com/questions/65944627/in-scala-how-could-a-covariant-type-parameter-be-an-upper-bound-of-an-abstract
// TODO: merge into shapeless Poly1
trait Poly1Base[IUB, OUB] {

  trait Case[-I <: IUB] {

    type Out <: OUB

    def apply(v: I): Out
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

  class ==>[
      -I <: IUB,
      O <: OUB
  ](val toOut: I => O)
      extends Case[I] {

    final type Out = O

    override def apply(v: I): O = toOut(v)
  }

  //  case class FromFn[
  //      -I <: IUB,
  //      O <: OUB
  //  ](fn: I => O)
  //      extends (I ==> O) {
  //
  //    final override def apply(v: I): O = fn(v)
  //  }

  def apply[I <: IUB]: Factory[I] = from[I]
  def from[I <: IUB] = new Factory[I]() // same as `at` in Poly1?

  protected class Factory[I <: IUB]() {

    def to[O <: OUB](fn: I => O): I ==> O = new (I ==> O)(fn)
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

//  object AsShapelessPoly2 extends Poly2 { TODO

//  case class ComposeDep1[F[??] <: DepFn1[??]]() extends Poly1Base[Any, OUB] {
//
//    implicit def chain[
//        S,
//        I <: IUB
//    ](
//        implicit
//        dep1: F[S] { type Out <: I },
//        _case: Poly1Base.this.Case[I]
//    ): S ==> _case.Out = from[S].to { ss =>
//      val i = dep1.apply(ss)
//      _case(i)
//    }
//  }
//
//  case class ComposeDep2[F[A, B] <: DepFn2[A, B]]() extends Poly1Base[Any, OUB] {
//
//    implicit def chain[
//        S1,
//        S2,
//        I <: IUB
//    ](
//        implicit
//        dep2: F[S1, S2] { type Out <: I },
//        _case: Poly1Base.this.Case[I]
//    ): (S1, S2) ==> _case.Out = from[(S1, S2)].to { ss =>
//      val i = dep2.apply(ss._1, ss._2)
//      _case(i)
//    }
//  }
}
