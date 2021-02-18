package org.shapesafe.core.shape.binary

import org.shapesafe.core.arity.Arity
import org.shapesafe.core.axis.Axis.->>
import shapeless.ops.record.Keys
import shapeless.{::, HList, NotContainsConstraint, Witness}

trait DistinctAppender extends FieldAppender {

  implicit def ifNewName[
      OLD <: HList,
      N <: String,
      D <: Arity,
      OLDNS <: HList
  ](
      implicit
      name: Witness.Aux[N],
      keys: Keys.Aux[OLD, OLDNS],
      isNew: NotContainsConstraint[OLDNS, N]
  ): ==>[(OLD, N ->> D), (N ->> D) :: OLD] = {

    from[(OLD, N ->> D)].==> {
      case (old, field) =>
        field.asInstanceOf[N ->> D] :: old
    }
  }
}
object DistinctAppender extends DistinctAppender
