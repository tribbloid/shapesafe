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

  case class ToBe[O <: OUB](override val out: O) extends Of[O]

  // doesn't extend T => R intentionally
  // each ProofSystem use a different one to alleviate search burden of compiler (or is it redundant?)

  trait CanProve[-I, +P <: Proof] {
    def apply(v: I): P

    final def valueOf(v: I): P#Out = apply(v).out
  }

  object CanProve {

    implicit def summonFor[I, P <: Proof](v: I)(
        implicit
        bound: CanProve[I, P]
    ): P = bound.apply(v)
  }

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
  class =>>^^[-I, P <: Proof](
      val toProof: I => P
  ) extends CanProve[I, P] {

    override def apply(v: I): P = toProof(v)
  }

  type ~~>[-I, +O <: OUB] = CanProve[I, Proof.Lt[O]]

  class =>>[-I, O <: OUB](
      val toOut: I => O
  ) extends =>>^^[I, ToBe[O]](v => ToBe(toOut(v)))

  def from[I]: Factory[I] = Factory[I]()

  case class Factory[I]() {

    def to[P <: Proof](fn: I => P) = new (I =>>^^ P)(fn)

    def out[O <: OUB](fn: I => O) = new (I =>> O)(fn)
  }

}
