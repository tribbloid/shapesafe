package org.shapesafe.core

import scala.language.implicitConversions

trait HasPropositions[OUB] {

  trait Proposition extends Serializable {

    type Repr <: OUB
  }
  case object Proposition extends CompanionOf[Proposition] {

    abstract class ^[O <: OUB] extends Proposition {
      final type Repr = O
    }
  }

  sealed trait CompanionOf[P <: Proposition] {

    type Aux[O <: OUB] = P {

      type Repr = O
    }

    type Lt[+O <: OUB] = P {

      type Repr <: O
    }

    type ^[O <: OUB] <: Aux[O]
  }

  case class Aye[O <: OUB](value: O) extends Proposition.^[O] {}

  case class Nay[O <: OUB](reason: Option[String] = None) extends Proposition.^[O] {}

  case class Incapable[O <: OUB]() extends Proposition.^[O] {}

  case class Absurd[O <: OUB](
      aye: Aye[O],
      nay: Nay[O]
  ) extends Proposition.^[O] {}
}

object HasPropositions {}
