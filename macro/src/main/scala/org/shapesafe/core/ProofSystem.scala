package org.shapesafe.core

import scala.language.implicitConversions

/**
  * If Poly1 works smoothly there will be no point in defining it, too bad the assumed compiler bug make it necessary
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

    type Lt[O <: OUB] = Proof {
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
    * representing 2 morphism from:
    * - value v to value apply(v)
    * - type I to type O
    * which is why it uses =>> instead of ==>
    * @tparam I from type
    * @tparam O to type
    */
  trait =>>[-I, +O <: Proof] {
    def apply(v: I): O
  }

  object =>> {

    implicit def summon[I, O <: Proof](v: I)(
        implicit
        bound: I =>> O
    ): O = bound.apply(v)

    implicit def trivial[I <: Proof]: I =>> I = identity
  }

  trait ForAll[-I, +O <: Proof] extends (I =>> O) {

    def prove: I => O

    final override def apply(in: I): O = prove(in)
  }
}
