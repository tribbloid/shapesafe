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
  type |-<[-I, O <: OUB] = Proof[I, system.Aye.Lt[O]]

  /**
    * entailment, logical implication used only in existential proof summoning
    */
  // TODO: how to override it in subclasses?
  @implicitNotFound(
    "[NO PROOF]\n${I}\n    |-\n${O}\n"
  )
  type |-[-I, O <: OUB] = Proof[I, system.Aye.Aux[O]]

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
  type =>>[-I, O <: OUB] <: Proof[I, system.Aye.^[O]]
  def fromFn[I, O <: OUB](_fn: I => O): I =>> O

  final def forAll[I]: ForAll[I] = new ForAll[I]
  final def forValue[I](v: I): ForAll[I] = forAll[I]

  class ForAll[I] {

    //    def =>>^^[P <: Consequent](_fn: I => P): I =>>^^ P = new (I =>>^^ P) {
    //
    //      override def apply(v: I): P = _fn(v)
    //    }

    def =>>[O <: OUB](_fn: I => O): I =>> O = fromFn(_fn)

//    def =>>[O <: OUB](_fn: I => O): I =>> O = new (I =>> O) {
//
//      override def apply(v: I): root.Aye.^[O] = root.Aye.^[O](_fn(v))
//    }

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

    //    trait =>>^^[-I, +P <: Consequent] extends Verdict[I, P]

  }

  // TODO: these are dead!
//  def satisfying[OB <: OUB] = new Satisfying[OB]()
//
//  class Satisfying[OB <: OUB]() {
//
//    case class If[I](v: I) {
//
//      implicit def findProof[O <: OB](
//          implicit
//          prove: I |- O
//      ): I |- O = prove
//
//      implicit def canProve_^^[O <: OB](
//          implicit
//          prove: I |- O
//      ): root.Proposition.Aux[O] = prove.apply(v)
//
//      implicit def canProve[O <: OB](
//          implicit
//          prove: I |- O
//      ): O = {
//
//        canProve_^^(prove).value
//      }
//    }
//  }

  class SubScope
      extends ProverScope.SubScopeLike[OUB](
        this
      )
}

object ProverScope {

  case class SubScopeLike[_OUB](
      outer: ProverScope { type OUB = _OUB }
  ) extends ProverScope {

    override val system: outer.system.type = outer.system

    final override type OUB = _OUB

    trait Proof[-I, +P <: Consequent] extends system.Proof[I, P]
//    trait Proof[-I, +P <: Consequent] extends outer.Proof[I, P]
    // TODO: I don't know how to combine the scope of theorems, decide it later

    class =>>[-I, O <: _OUB](_fn: I => O) extends Proof[I, system.Aye.^[O]] {
      override def apply(v: I): system.Aye.^[O] = {
        val out = _fn(v)
        system.Aye.^(out)
      } // TODO: simplify
    }

    override def fromFn[I, O <: _OUB](_fn: I => O): I =>> O = new =>>(_fn)
  }
}
