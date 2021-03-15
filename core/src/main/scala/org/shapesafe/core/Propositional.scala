package org.shapesafe.core

import scala.language.implicitConversions

trait Propositional[OUB] {

  trait Term extends Serializable {

    type Domain <: OUB
    def value: Domain
  }

  case object Term {

    type Aux[O <: OUB] = Term {
      type Domain = O
    }

    type Lt[+O <: OUB] = Term {
      type Domain <: O
    }

    trait Of[O <: OUB] extends Term {
      final type Domain = O
    }

    case class ToBe[O <: OUB](val value: O) extends Of[O]
  }
}

//object Propositional {
//
//  trait PropositionBase extends Serializable {
//
//    type Codomain
//    def value: Codomain
//  }
//}
