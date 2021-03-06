package org.shapesafe.core.tuple

trait CanCons {
  _self: TupleSystem =>

  // TODO:too much boilerplate, switch to ~~> Proof pattern or Poly1/Poly2?
  trait Cons[-TAIL <: Tuple, -HEAD <: UpperBound] {

    type ConsResult <: Tuple

    def apply(tail: TAIL, head: HEAD): ConsResult
  }

  object Cons {

    def from[TAIL <: Tuple, HEAD <: UpperBound] = new Factory[TAIL, HEAD]

    class Factory[TAIL <: Tuple, HEAD <: UpperBound] {

      def to[O <: Tuple](fn: (TAIL, HEAD) => O) = new FromFn2[TAIL, HEAD, O](fn)
    }

    case class FromFn2[-TAIL <: Tuple, -HEAD <: UpperBound, O <: Tuple](
        fn: (TAIL, HEAD) => O
    ) extends Cons[TAIL, HEAD] {

      final type ConsResult = O

      final override def apply(tail: TAIL, head: HEAD): ConsResult = fn(tail, head)
    }

    def summonFor[TAIL <: Tuple, HEAD <: UpperBound](tail: TAIL, head: HEAD)(
        implicit
        ev: Cons[TAIL, HEAD]
    ): ev.type = ev

    def apply[TAIL <: Tuple, HEAD <: UpperBound](tail: TAIL, head: HEAD)(
        implicit
        ev: Cons[TAIL, HEAD]
    ): ev.ConsResult = {

      ev.apply(tail, head)
    }
  }
}
