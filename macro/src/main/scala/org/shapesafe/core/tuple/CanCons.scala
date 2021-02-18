package org.shapesafe.core.tuple

trait CanCons {
  _self: TupleSystem =>

  // TODO:too much boilerplate, switch to ~~> Proof pattern or Poly1/Poly2?
  trait Cons[-TAIL <: Impl, -HEAD <: UpperBound] {

    type Out <: Impl

    def apply(tail: TAIL, head: HEAD): Out
  }

  object Cons {

    def from[TAIL <: Impl, HEAD <: UpperBound] = new Factory[TAIL, HEAD]

    class Factory[TAIL <: Impl, HEAD <: UpperBound] {

      def to[O <: Impl](fn: (TAIL, HEAD) => O) = new FromFn2[TAIL, HEAD, O](fn)
    }

    case class FromFn2[-TAIL <: Impl, -HEAD <: UpperBound, O <: Impl](
        fn: (TAIL, HEAD) => O
    ) extends Cons[TAIL, HEAD] {

      final type Out = O

      final override def apply(tail: TAIL, head: HEAD): Out = fn(tail, head)
    }

    def summonFor[TAIL <: Impl, HEAD <: UpperBound](tail: TAIL, head: HEAD)(
        implicit
        ev: Cons[TAIL, HEAD]
    ): ev.type = ev

    def apply[TAIL <: Impl, HEAD <: UpperBound](tail: TAIL, head: HEAD)(
        implicit
        ev: Cons[TAIL, HEAD]
    ): ev.Out = {

      ev.apply(tail, head)
    }
  }
}
