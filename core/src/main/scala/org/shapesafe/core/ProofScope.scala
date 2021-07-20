package org.shapesafe.core

import scala.annotation.implicitNotFound
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

  // TODO: this should potentially be merged with Refutation cases in MsgBroker
  //  Such that successful proof can show reasoning at runtime.
  //  At this moment, this feature is implemented in PeekReporter
  //  which is too complex for its own good
  type Proof[-I, +P <: Consequent] <: root.Proof[I, P]

  /**
    * entailment, logical implication used only in existential proof summoning
    */
  // TODO: how to override it in subclasses?
  @implicitNotFound(
    "[NO PROOF]\n${I}\n    |-\n??? <: ${O}\n"
  )
  type |-<[-I, O <: OUB] = Proof[I, root.Term.Lt[O]]

  @implicitNotFound(
    "[NO PROOF]\n${I}\n    |-\n${O}\n"
  )
  type |-[-I, O <: OUB] = Proof[I, root.Term.Aux[O]]

  def forAll[I]: root.ForAll[I]

  def satisfying[OB <: OUB] = new Satisfying[OB]()

  class Satisfying[OB <: OUB]() {

    case class If[I](v: I) {

      implicit def findProof[O <: OB](
          implicit
          prove: I |- O
      ): I |- O = prove

      implicit def canProve_^^[O <: OB](
          implicit
          prove: I |- O
      ): root.Term.Aux[O] = prove.apply(v)

      implicit def canProve[O <: OB](
          implicit
          prove: I |- O
      ): O = {

        canProve_^^(prove).value
      }
    }
  }
}

object ProofScope {

  case class ChildScope[O](root: ProofSystem[O]) extends ProofScope {

    type OUB = O

    trait Proof[-I, +P <: Consequent] extends root.Proof[I, P]

    override def forAll[I]: ForAll[I] = new ForAll[I] {}

    object ForAll {

      trait =>>^^[-I, +P <: Consequent] extends Proof[I, P] with root.ForAll.=>>^^[I, P]

      trait =>>[-I, O <: OUB] extends =>>^^[I, root.Term.^[O]] with root.ForAll.=>>[I, O]
    }

    trait ForAll[I] extends root.ForAll[I] {

      import ForAll._

      override def =>>^^[P <: Consequent](_fn: I => P) = new (I =>>^^ P) {
        override def apply(v: I): P = _fn(v)
      }

      override def =>>[O <: OUB](_fn: I => O): I =>> O = new (I =>> O) {
//        override def valueOf(v: I): O = fn(v)
        override def apply(v: I): root.Term.^[O] = root.Term.^[O](_fn(v))
      }
    }
  }
}
