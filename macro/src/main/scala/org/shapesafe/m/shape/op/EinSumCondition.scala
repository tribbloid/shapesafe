package org.shapesafe.m.shape.op

import org.shapesafe.m.Poly1Base
import org.shapesafe.m.arity.binary.AssertEqual
import org.shapesafe.m.arity.{Arity, Expression}
import org.shapesafe.m.axis.Axis.->>
import shapeless.ops.record.{Keys, Selector}
import shapeless.{::, HList, NotContainsConstraint, Witness}

trait EinSumCondition extends Poly1Base[(HList, (_ <: String) ->> Expression), HList] {

  import org.shapesafe.m.arity.ProveArity._

  implicit def IfExistingName[
      OLD <: HList,
      N <: String,
      D1 <: Expression,
      D2 <: Expression,
      O <: Arity
  ](
      implicit
      name: Witness.Aux[N],
      selector: Selector.Aux[OLD, N, D1],
      lemma: AssertEqual[D1, D2] ~~> Proof.Aux[O]
  ): =:=>[(OLD, N ->> D2), (N ->> O) :: OLD] = {

    buildFrom[(OLD, N ->> D2)].to {

      case (old, field) =>
        import shapeless.record._

        val d1 = old.apply(name)
        val d2 = field

        val d_new: O = lemma.apply(AssertEqual(d1, d2)).out

        d_new.asInstanceOf[N ->> O] :: old
    }

  }

}

object EinSumCondition extends EinSumCondition {

  implicit def IfNewName[
      OLD <: HList,
      N <: String,
      D <: Expression,
      OLDNS <: HList
  ](
      implicit
      name: Witness.Aux[N],
      keys: Keys.Aux[OLD, OLDNS],
      NotContainsConstraint: NotContainsConstraint[OLDNS, N]
  ): =:=>[(OLD, N ->> D), (N ->> D) :: OLD] = {

    buildFrom[(OLD, N ->> D)].to {
      case (old, field) =>
        field.asInstanceOf[N ->> D] :: old
    }
  }
}
