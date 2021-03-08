package org.shapesafe.core

import scala.language.implicitConversions

/**
  * This trait forms the backbone of compile-time reasoning and should reflect our best effort in reproducing
  * Curry-Howard isomorphism with scala compiler (regardless of how ill-suited it is), expecting drastic changes in case
  * the implicit search algorithm was improved
  * @tparam O upper bound of output
  */
// TODO: If Poly1 works smoothly it could totally supersedes this class, too bad the assumed compiler bug made it necessary
trait ProofSystem[O] extends Propositional[O] with ProofScope { // TODO: no IUB?

  type OUB = O

  final val root: this.type = this

  trait Proof[-I, +P <: Consequent] {
    def apply(v: I): P

    final def valueOf(v: I): P#Domain = apply(v).value
  }

  def forAll[I]: Factory[I] = new Factory[I] {}

  final def fromValue[I](v: I): Factory[I] = forAll[I]

  object Factory {

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
    trait =>>[-I, O <: OUB] extends =>>^^[I, root.Term.ToBe[O]] {}
  }

  trait Factory[I] {

    import Factory._

    def =>>^^[P <: Consequent](_fn: I => P) = new (I =>>^^ P) {
      override def apply(v: I): P = _fn(v)
    }

    def =>>[O <: OUB](_fn: I => O) = new (I =>> O) {
//      override def valueOf(v: I): O = _fn(v)

      override def apply(v: I): root.Term.ToBe[O] = root.Term.ToBe[O](_fn(v))
    }

    def to[O <: OUB] = new To[O]

    class To[OB <: OUB] {

      def entails[O <: OB](v: I)(
          implicit
          prove: I |- O
      ): Term.Aux[O] = prove.apply(v)

      def entailsValue[O <: OB](v: I)(
          implicit
          prove: I |- O
      ): O = prove.apply(v).value
    }
  }

  class SubScope extends ProofScope.Child(this)
}
