package org.shapesafe.core.tuple

import org.shapesafe.core.Poly1Base
import shapeless.{HList, HNil}

trait TupleSystem {

  type UpperBound

  type Impl

  type Eye <: Impl
  def Eye: Eye

//  type ><[
//      TAIL <: Impl,
//      HEAD <: UpperBound
//  ] <: Impl

  trait AbstractFromHList extends Poly1Base[HList, Impl] {

    implicit val toEye: HNil ==> Eye = {
      forAll[HNil].==> { _ =>
        Eye
      }
    }
  }
}

object TupleSystem {}
