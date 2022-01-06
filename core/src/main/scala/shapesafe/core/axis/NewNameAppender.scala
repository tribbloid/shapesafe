package shapesafe.core.axis

import shapesafe.core.arity.Arity
import shapesafe.core.axis.Axis.->>
import shapeless.ops.record.Keys
import shapeless.{::, HList, NotContainsConstraint, Witness}

trait NewNameAppender extends RecordUpdater {

  implicit def ifNewName[
      OLD <: HList,
      N <: String,
      A1 <: Arity,
      OLDNS <: HList
  ](
      implicit
      name: Witness.Aux[N],
      keys: Keys.Aux[OLD, OLDNS],
      isNew: NotContainsConstraint[OLDNS, N]
  ): (OLD, N ->> A1) =>> ((N ->> A1) :: OLD) = {

    forAll[(OLD, N ->> A1)].=>> {
      case (old, field) =>
        field :: old
    }
  }
}

object NewNameAppender extends NewNameAppender
