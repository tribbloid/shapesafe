package org.shapesafe.core

import scala.language.implicitConversions

trait HasProposition[OUB] {

  trait Proposition extends Serializable {

    type Repr <: OUB
    def value: Repr
    // in many languages, all Propositions have a single runtime value (SProp)
    // we don't use this design, it is still much easier to read and debug propositions in runtime
  }

  case object Proposition {

    type Aux[O <: OUB] = Proposition {

      type Repr = O
    }

    type Lt[+O <: OUB] = Proposition {

      type Repr <: O
    }

    case class ^[O <: OUB](value: O) extends Proposition {

      final type Repr = O
    }
  }
}
