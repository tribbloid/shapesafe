package org.shapesafe.core.tuple

import org.shapesafe.core.Poly1Base
import shapeless.{HList, HNil}

trait TupleSystem {

  type VBound

  type Tuple

  type Eye <: Tuple
  val Eye: Eye
//  final type Eye = Eye.type

  trait HListIntake extends Poly1Base[HList, Tuple] {

    final val outer = TupleSystem.this

    implicit val toEye: HNil ==> Eye = {
      forAll[HNil].==> { _ =>
        Eye
      }
    }
  }

  object HListIntake {}
}

object TupleSystem {}
