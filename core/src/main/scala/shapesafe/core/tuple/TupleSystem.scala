package shapesafe.core.tuple

import shapesafe.core.AdHocPoly1
import shapeless.HNil

trait TupleSystem {

  type VBound

  type Tuple

  type Eye <: Tuple
  val Eye: Eye
//  final type Eye = Eye.type

  trait HListIntake extends AdHocPoly1 {

    final val outer = TupleSystem.this

    implicit val toEye: HNil =>> Eye = {
      at[HNil].defining { _ =>
        Eye
      }
    }
  }

  object HListIntake {}
}

object TupleSystem {}
