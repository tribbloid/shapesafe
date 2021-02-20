package org.shapesafe.core

import scala.language.implicitConversions

/**
  * If Poly1 works smoothly there will be no point in defining it, too bad the assumed compiler bug made it necessary
  *
  * @tparam OUB upper bound of output
  */
trait ProofScope { // TODO: no IUB?

  type OUB

  val root: ProofSystem[OUB]

  type Consequent = root.Term

  type =>>^^[-I, +P <: Consequent] <: root.=>>^^[I, P]

  /**
    * entailment, logical implication used only in existential proof summoning
    */
  type |~~[-I, O <: OUB] = =>>^^[I, root.Term.Lt[O]]
  type |--[-I, O <: OUB] = =>>^^[I, root.Term.Aux[O]]

  type =>>[-I, O <: OUB] <: root.=>>[I, O]

  def forAll[I]: root.Factory[I]

  def satisfying[OB <: OUB] = new Satisfying[OB]()

  class Satisfying[OB <: OUB]() {

    case class If[I](v: I) {

      implicit def findProof[O <: OB](
          implicit
          prove: I |-- O
      ): I |-- O = prove

      implicit def canProve_^^[O <: OB](
          implicit
          prove: I |-- O
      ): root.Term.Aux[O] = prove.apply(v)

      implicit def canProve[O <: OB](
          implicit
          prove: I |-- O
      ): O = {

        canProve_^^(prove).value
      }
    }
  }
}

object ProofScope {

  case class Child[O](root: ProofSystem[O]) extends ProofScope {

    type OUB = O

    trait =>>^^[-I, +P <: Consequent] extends root.=>>^^[I, P]

    trait =>>[-I, O <: OUB] extends root.=>>[I, O]

    override def forAll[I]: Factory[I] = new Factory[I] {}

    trait Factory[I] extends root.Factory[I] {

      override def =>>^^[P <: Consequent](_fn: I => P) = new (I =>>^^ P) {
        override def apply(v: I): P = _fn(v)
      }

      override def =>>[O <: OUB](_fn: I => O): I =>> O = new (I =>> O) {
//        override def valueOf(v: I): O = fn(v)
        override def apply(v: I): root.Term.ToBe[O] = root.Term.ToBe[O](_fn(v))
      }
    }
  }
}
