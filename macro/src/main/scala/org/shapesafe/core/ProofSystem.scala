package org.shapesafe.core

import scala.language.implicitConversions

/**
  * If Poly1 works smoothly there will be no point in defining it, too bad the assumed compiler bug made it necessary
  * @tparam OUB upper bound of output
  */
trait ProofSystem[OUB] { // TODO: no IUB?

  trait Proposition extends Serializable {

    type Codomain <: OUB
    def value: Codomain
  }

  case object Proposition {

    type Aux[O <: OUB] = Proposition {
      type Codomain = O
    }

    type Lt[+O <: OUB] = Proposition {
      type Codomain <: O
    }

    // Can't use Aux, syntax not supported by scala
    trait Of[O <: OUB] extends Proposition {
      final type Codomain = O
    }

    case class ToBe[O <: OUB](override val value: O) extends Of[O]
  }

  // doesn't extend T => R intentionally
  // each ProofSystem use a different one to alleviate search burden of compiler (or is it redundant?)

  trait CanProve[-I, +P <: Proposition] {
    def apply(v: I): P

    final def valueOf(v: I): P#Codomain = apply(v).value // TODO: this should be real apply? The above should be 'prove'
  }

  object CanProve {}

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
  class =>>^^[-I, P <: Proposition](
      val toProof: I => P
  ) extends CanProve[I, P] {

    override def apply(v: I): P = toProof(v)
  }

  type -->[-I, O <: OUB] = CanProve[I, Proposition.Aux[O]]
  type ~~>[-I, +O <: OUB] = CanProve[I, Proposition.Lt[O]]

  class =>>[-I, O <: OUB](
      val toOut: I => O
  ) extends =>>^^[I, Proposition.ToBe[O]](v => Proposition.ToBe(toOut(v)))

  def from[I]: Factory[I] = new Factory[I]()

  class Factory[I]() {

    def =>>^^[P <: Proposition](fn: I => P) = new (I =>>^^ P)(fn)

    def =>>[O <: OUB](fn: I => O) = new (I =>> O)(fn)

  }

  def at[I](v: I) = new Summoner[I](v)

  class Summoner[I](v: I) extends Factory[I] {

    implicit def summon[P <: Proposition](
        implicit
        ev: CanProve[I, P]
    ): P = ev.apply(v)

    implicit def summonValue[P <: Proposition](
        implicit
        ev: CanProve[I, P]
    ): P#Codomain = summon(ev).value
  }
}
