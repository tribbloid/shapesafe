package shapesafe.core.axis

import ai.acyclic.prover.commons.refl.XString
import shapesafe.core.arity.binary.Op2Like
import shapesafe.core.arity.{ArityType, LeafArity}
import shapesafe.core.axis.Axis.->>
import shapeless.ops.record.{Modifier, Selector}
import shapeless.{::, HList}

class OldNameUpdaters[OP <: Op2Like](val op: OP) {

  trait Appender extends RecordUpdater {
    // TODO: should be a Poly2?
    // TODO: merge into EinSumIndexed.Cons

    import shapesafe.core.arity.ProveArity._

    implicit def ifOldName[
        OLD <: HList,
        N <: XString,
        A1 <: ArityType,
        A2 <: ArityType,
        O <: LeafArity
    ](
        implicit
        name: ValueOf[N],
        selector: Selector.Aux[OLD, N, A1],
        lemma: op.On[A1, A2] |- O
    ): (OLD, N ->> A2) =>> ((N ->> O) :: OLD) = {

      at[(OLD, N ->> A2)].defining {

        case (old, field) =>
          import shapeless.record._

          val d1 = selector.apply(old)
          val d2 = field: A2

          val oped = op.on(d1.^, d2.^)

          val d_new: O = lemma.consequentFor(oped).value
          d_new.asInstanceOf[N ->> O] :: old
      }
    }
  }
  object Appender extends Appender

  trait Reducer extends RecordUpdater {
    // TODO: should be a Poly2?
    // TODO: merge into EinSumIndexed.Cons

    import shapesafe.core.arity.ProveArity._

    implicit def ifOldName[
        OLD <: HList,
        N <: XString,
        A1 <: ArityType,
        A2 <: ArityType,
        O <: LeafArity
    ](
        implicit
        name: N,
        selector: Selector.Aux[OLD, N, A1], // TODO: how to remove this? should be implied in modifier
        lemma: op.On[A1, A2] |- O,
        modifier: Modifier[OLD, N, A1, O]
    ): (OLD, N ->> A2) =>> modifier.Out = {

      at[(OLD, N ->> A2)].defining {

        case (old, field) =>
          val a2 = field: A2

          val result = modifier.apply(
            old,
            { a1 =>
              val o: O = lemma.consequentFor(op.on(a1.^, a2.^)).value
              o
            }
          )

          result
      }
    }
  }
  object Reducer extends Reducer
}

object OldNameUpdaters {}
