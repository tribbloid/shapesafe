package shapesafe.core.tuple

import shapesafe.core.AdHocPoly1
import shapeless.{HList, HNil}

trait TupleSystem {

  type VBound

  type Tuple

  type Eye <: Tuple
  val Eye: Eye
//  final type Eye = Eye.type

  trait HListIntake extends AdHocPoly1[HList, Tuple] {

    final val outer = TupleSystem.this

    implicit val toEye: HNil =>> Eye = {
      forAll[HNil].=>> { _ =>
        Eye
      }
    }
  }

  object HListIntake {}
}

object TupleSystem {}
