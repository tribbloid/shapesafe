package org.shapesafe.core.tuple

import org.shapesafe.core.Poly1Base
import shapeless.{HList, HNil}

trait TupleSystem {

  type VBound

  type Tuple

  type Eye <: Tuple
  def Eye: Eye

  trait AbstractFromHList extends Poly1Base[HList, Tuple] {

    final val outer = TupleSystem.this

    implicit val toEye: HNil ==> Eye = {
      forAll[HNil].==> { _ =>
        Eye
      }
    }
  }
}

object TupleSystem {}
