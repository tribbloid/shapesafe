package shapesafe.core.logic

object ParadoxDeduction {

  trait _Imp0 extends HasTheory {
    import theory._

    implicit def contradicting2[I, O](
        implicit
        refuting: I |-\- O,
        proving: I |- O
    ): Theorem[I `_|_` O] = =>><<=[I, O]
  }
}

trait ParadoxDeduction extends ParadoxDeduction._Imp0 {
  import theory._

  implicit def contradicting1[I, O](
      implicit
      proving: I |- O,
      refuting: I |-\- O
  ): Theorem[I `_|_` O] = =>><<=[I, O]
}
