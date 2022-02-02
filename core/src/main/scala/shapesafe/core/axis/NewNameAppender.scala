package shapesafe.core.axis

import shapeless.ops.record.Keys
import shapeless.{::, HList, NotContainsConstraint, Witness}
import shapesafe.core.arity.ArityType
import shapesafe.core.axis.Axis.->>

trait NewNameAppender extends RecordUpdater {

  implicit def ifNewName[
      OLD <: HList,
      N <: String,
      A1 <: ArityType,
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

//  implicit def ifNoName[
//      OLD <: HList,
//      A1 <: ArityType
//  ]: (OLD, Const.NoName ->> A1) =>> ((Const.NoName ->> A1) :: OLD) = {
//
//    forAll[(OLD, Const.NoName ->> A1)].=>> {
//      case (old, field) =>
//        field :: old
//    }
//  }
}

object NewNameAppender extends NewNameAppender
