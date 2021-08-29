package org.shapesafe.core

import scala.annotation.implicitNotFound
import scala.language.implicitConversions

/**
  * If Poly1 works smoothly there will be no point in defining it, too bad the assumed compiler bug made it necessary
  *
  * @tparam OUB upper bound of output
  */
trait ProverScope { // TODO: no IUB?

  type OUB

  val system: ProverSystem[OUB]

  type Consequent = system.Proposition

  // TODO: this should potentially be merged with Refutation cases in MsgBroker
  //  Such that successful proof can show reasoning at runtime.
  //  At this moment, this feature is implemented in PeekReporter
  //  which is too complex for its own good
  type Proof[-I, +P <: Consequent] <: system.Proof[I, P]

  @implicitNotFound(
    "[NO PROOF]\n${I}\n    |-\n??? <: ${O}\n"
  )
  final type |-<[-I, O <: OUB] = Proof[I, system.Aye[_ <: O]]

  /**
    * entailment, logical implication used only in existential proof summoning
    */
  // TODO: how to override it in subclasses?
  @implicitNotFound(
    "[NO PROOF]\n${I}\n    |-\n${O}\n"
  )
  final type |-[-I, O <: OUB] = Proof[I, system.Aye[O]]

  final type |-\-[-I, O <: OUB] = Proof[I, system.Nay[O]]

  final type |-?-[-I, O <: OUB] = Proof[I, system.Grey[O]]

  /**
    * Logical implication: If I is true then P is definitely true (or: NOT(I) /\ P = true)
    * NOT material implication! If I can be immediately refuted then it implies NOTHING! Not even itself.
    *
    * In fact, any [[Arity]] or [[Shape]] that cannot be refuted at compile-time should subclass [[VerifiedArity]]
    * or [[VerifiedShape]], which implies itself
    *
    * Programmer must ensure that no implicit subclass is defined for immediately refutable conjectures
    *
    * the symbol =>> is there to stress that it represents 2 morphism:
    *
    * - value v --> value apply(v)
    *
    * - domain I --> domain O
    * @tparam I src type
    * @tparam O tgt type
    */
  def =>>[I, O <: OUB](_fn: I => O): I |- O

  def =\>>[I, O <: OUB](): I |-\- O

  def =?>>[I, O <: OUB](): I |-?- O

  final def forAll[I]: ForAll[I] = new ForAll[I]
  final def forValue[I](v: I): ForAll[I] = forAll[I]

  class ForAll[I] {

    def =>>[O <: OUB](_fn: I => O): I |- O = ProverScope.this.=>>(_fn) // TODO: reflective call!

    def summon[O <: OUB](
        implicit
        prove: I |- O
    ): I |- O = prove

    def to[O <: OUB] = new To[O]

    class To[OB <: OUB] {

      def summon[O <: OB](
          implicit
          prove: I |- O
      ): I |- O = prove
    }
  }

  abstract class SubScope extends ProverScope {

    final val outer: ProverScope.this.type = ProverScope.this
    final override val system: outer.system.type = outer.system

    final override type OUB = outer.OUB

    override type Proof[-I, +P <: Consequent] <: outer.Proof[I, P]

//    override def fromFn[I, O <: OUB](_fn: I => O): Proof[I, system.Aye[O]]
  }
}

object ProverScope {}
