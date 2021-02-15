package org.shapesafe.core

import scala.language.implicitConversions

/**
  * If Poly1 works smoothly there will be no point in defining it, too bad the assumed compiler bug made it necessary
  * @tparam OUB upper bound of output
  */
trait ProofSystem[OUB] { // TODO: no IUB?

  trait Proof extends Serializable {

    type Out <: OUB
    def out: Out
  }

  case object Proof {

    type Aux[O <: OUB] = Proof {
      type Out = O
    }

    type Lt[+O <: OUB] = Proof {
      type Out <: O
    }
  }

  // Can't use Aux, syntax not supported by scala
  trait Of[O <: OUB] extends Proof {
    final type Out = O
  }

  // doesn't extend T => R intentionally
  // each ProofSystem use a different one to alleviate search burden of compiler (or is it redundant?)

  /**
    * representing 2 morphism:
    *
    * - value v --> value apply(v)
    *
    * - type I --> type O
    *
    * which is why it uses =>>
    * @tparam I src type
    * @tparam P tgt type
    */
  trait =>>[-I, +P <: Proof] {
    def apply(v: I): P

    def valueOf(v: I): P#Out = apply(v).out
  }

  object =>> {

    implicit def summonFor[I, P <: Proof](v: I)(
        implicit
        bound: I =>> P
    ): P = bound.apply(v)

    implicit def trivial[I <: Proof]: I =>> I = identity
  }

  type ~~>[-I, +Out <: OUB] = I =>> Proof.Lt[Out]
}
