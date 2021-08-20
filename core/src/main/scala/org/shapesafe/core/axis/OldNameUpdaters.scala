package org.shapesafe.core.axis

import org.shapesafe.core.arity.binary.Op2Like
import org.shapesafe.core.arity.{Arity, LeafArity}
import org.shapesafe.core.axis.Axis.->>
import shapeless.ops.record.{Modifier, Selector}
import shapeless.syntax.RecordOps
import shapeless.{::, HList, Witness}

class OldNameUpdaters[OP <: Op2Like](val op: OP) {

  trait Appender extends RecordUpdater {
    // TODO: should be a Poly2?
    // TODO: merge into EinSumIndexed.Cons

    import org.shapesafe.core.arity.ProveArity._

    implicit def ifOldName[
        OLD <: HList,
        N <: String,
        A1 <: Arity,
        A2 <: Arity,
        O <: LeafArity
    ](
        implicit
        name: Witness.Aux[N],
        selector: Selector.Aux[OLD, N, A1],
        lemma: op.On[A1, A2] |- O
    ): (OLD, N ->> A2) ==> ((N ->> O) :: OLD) = {

      forAll[(OLD, N ->> A2)].==> {

        case (old, field) =>
          import shapeless.record._

          val d1 = old.apply(name)
          val d2 = field: A2

          val oped = op.on(d1.^, d2.^)

          val d_new: O = lemma.apply(oped).value
          d_new.asInstanceOf[N ->> O] :: old
      }
    }
  }
  //  object Appender extends Appender

  trait Squasher extends RecordUpdater {
    // TODO: should be a Poly2?
    // TODO: merge into EinSumIndexed.Cons

    import org.shapesafe.core.arity.ProveArity._

    implicit def ifOldName[
        OLD <: HList,
        N <: String,
        A1 <: Arity,
        A2 <: Arity,
        O <: LeafArity
    ](
        implicit
        name: Witness.Aux[N],
        selector: Selector.Aux[OLD, N, A1],
        lemma: op.On[A1, A2] |- O,
        modifier: Modifier[OLD, N, A1, O]
    ): (OLD, N ->> A2) ==> modifier.Out = {

      forAll[(OLD, N ->> A2)].==> {

        case (old, field) =>
          val a2 = field: A2

          val result = modifier.apply(
            old,
            { a1 =>
              val o: O = lemma.apply(op.on(a1.^, a2.^)).value
              o
            }
          )

          result
      }
    }
  }
  //  object Squasher extends Squasher
}

object OldNameUpdaters {}
