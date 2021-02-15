package org.shapesafe.core.tuple

trait CanCons {
  _self: TupleSystem =>

  // TODO:too cumbersome to define impl, switch to ~~> Proof pattern or Poly1Group?
  trait Cons[-TAIL <: Impl, -HEAD <: UpperBound] {

    type Out <: Impl

    def apply(tail: TAIL, head: HEAD): Out
  }

  object Cons {

    def apply[TAIL <: Impl, HEAD <: UpperBound] = new Factory[TAIL, HEAD]

    class Factory[TAIL <: Impl, HEAD <: UpperBound] {

      def to[O <: Impl](fn: (TAIL, HEAD) => O) = new FromFn[TAIL, HEAD, O](fn)
    }

    case class FromFn[-TAIL <: Impl, -HEAD <: UpperBound, O <: Impl](
        fn: (TAIL, HEAD) => O
    ) extends Cons[TAIL, HEAD] {

      final type Out = O

      final override def apply(tail: TAIL, head: HEAD): Out = fn(tail, head)
    }

    def peek[TAIL <: Impl, HEAD <: UpperBound](tail: TAIL, head: HEAD)(
        implicit
        ev: Cons[TAIL, HEAD]
    ): ev.type = ev
  }

  //  object Cons extends ProofSystem[(Impl, UpperBound)] {}
  //  import Cons._
  //
  //  type Cons[-TAIL <: Impl, -HEAD <: UpperBound] = (TAIL, HEAD) ~~> Cons.Proof
}
