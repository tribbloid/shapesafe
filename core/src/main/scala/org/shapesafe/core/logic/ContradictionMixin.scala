package org.shapesafe.core.logic

object ContradictionMixin {

  trait _Imp0 {
    self: Theory =>

    implicit def contradicting2[I, O](
        implicit
        refuting: I |-\- O,
        proving: I |- O
    ): Axiom[I `_|_` O] = =>><<=[I, O]
  }
}

trait ContradictionMixin extends ContradictionMixin._Imp0 {
  self: Theory =>

  implicit def contradicting1[I, O](
      implicit
      proving: I |- O,
      refuting: I |-\- O
  ): Axiom[I `_|_` O] = =>><<=[I, O]
}
