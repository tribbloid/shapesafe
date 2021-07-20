package org.shapesafe.core

import scala.language.implicitConversions

/**
  * This trait forms the backbone of compile-time reasoning and should reflect our best effort in reproducing
  * Curry-Howard isomorphism with scala compiler (regardless of how ill-suited it is), expecting drastic changes in case
  * the implicit search algorithm was improved
  * @tparam _OUB upper bound of output
  */
// TODO: If Poly1 works smoothly it could totally supersedes this class, too bad the assumed compiler bug made it necessary
trait ProofSystem[_OUB] extends Propositional[_OUB] with ProofScope { // TODO: no IUB?

  type OUB = _OUB

  final val root: this.type = this

  trait Proof[-I, +P <: Consequent] {
    def apply(v: I): P

    final def valueOf(v: I): P#Domain = apply(v).value
  }
  //TODO: add refute that supersedes def refute in type classes

  def forAll[I]: ForAll[I] = new ForAll[I] {}

  final def forValue[I](v: I): ForAll[I] = forAll[I]

  object ForAll {

    trait =>>^^[-I, +P <: Consequent] extends Proof[I, P]

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
    trait =>>[-I, O <: OUB] extends =>>^^[I, root.Term.^[O]] {}
  }

  trait ForAll[I] {

    import ForAll._

    def =>>^^[P <: Consequent](_fn: I => P): I =>>^^ P = new (I =>>^^ P) {
      override def apply(v: I): P = _fn(v)
    }

    def =>>[O <: OUB](_fn: I => O): I =>> O = new (I =>> O) {
//      override def valueOf(v: I): O = _fn(v)

      override def apply(v: I): root.Term.^[O] = root.Term.^[O](_fn(v))
    }

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

  class SubScope extends ProofScope.ChildScope(this)
}
