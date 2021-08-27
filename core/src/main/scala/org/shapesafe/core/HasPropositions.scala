package org.shapesafe.core

import scala.language.implicitConversions

trait HasPropositions[OUB] {

  trait Proposition extends Serializable {

    type Repr <: OUB
  }
  case object Proposition extends CompanionOf[Proposition] {}

  sealed trait CompanionOf[P <: Proposition] {

    type Aux[O <: OUB] = P {

      type Repr = O
    }

    type Lt[+O <: OUB] = P {

      type Repr <: O
    }

    type ^[O <: OUB] <: Aux[O]
  }

  trait Aye extends Proposition {

    def value: Repr
  }
  case object Aye extends CompanionOf[Aye] {

    case class ^[O <: OUB](override val value: O) extends Aye {

      final type Repr = O
    }
  }

  trait Nay extends Proposition {
    // add reasons
  }
  case object Nay extends CompanionOf[Nay]

  trait Incapable extends Proposition {}
  case object Incapable extends CompanionOf[Incapable]

  trait Absurd extends Proposition {
    def aye: Aye.Aux[Repr]
    def nay: Nay.Aux[Repr]
  }
  case object Absurd extends CompanionOf[Absurd]
}

object HasPropositions {}
