package org.shapesafe.core

import scala.annotation.implicitNotFound

trait ProofScope extends HasTactic { // TODO: no IUB?

  type OUB

  type UpcastEvidence

  type System <: ProofSystem.Aux[OUB] with Singleton
  val system: System

  type Consequent = System#Proposition

  // TODO: this should potentially be merged with Refutation cases in MsgBroker
  //  Such that successful proof can show reasoning at runtime.
  //  At this moment, this feature is implemented in PeekReporter
  //  which is too complex for its own good

  // constructive proof
  abstract class Proof[-I, +P <: Consequent] extends ProofLike {

    def scope: ProofScope.this.type = ProofScope.this

    def consequentFor(v: I): P

    final def instanceFor(v: I): P#Repr = {

      val verdict: P = consequentFor(v)
      verdict match {
        case y: system.Aye[_] =>
          y.asInstanceOf[system.Aye[P#Repr] with P].value // TODO: unsafe?
        case _ =>
          throw new UnsupportedOperationException(
            s"$verdict is not a constructive proposition"
          )
      }
    }

    final def apply(v: I): P#Repr = instanceFor(v)

    //    final def findApplicable(v: I): this.type = this
  }

  object Proof {

    case class Chain[ // a.k.a hypothetical syllogism
        A,
        B <: OUB,
        C <: OUB
    ](
        lemma1: A |- B,
        lemma2: B |- C
    ) extends (A |- C) {

      override def consequentFor(v: A): system.Aye[C] = {

        system.Aye(
          lemma2.instanceFor(lemma1.instanceFor(v))
        )
      }
    }
  }

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

  final type |-?-[-I, O <: OUB] = Proof[I, system.Abstain[O]]

  final type `_|_`[-I, O <: OUB] = Proof[I, system.Absurd[O]]

  /**
    *  TODO: this is the counterpart of Spark polymorphic function in the proof system
    *    required for defining axiom of induction
    *    at this moment it is useless
    */
  trait GenProof {

    type Specialised[I, O <: Consequent] <: Proof[I, O]

    def specialise[I, O <: Consequent]: Specialised[I, O]
  }

  object GenProof {}

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
  def =>>[I, O <: OUB](_fn: I => O): I |- O = { v =>
    val out = _fn(v)
    system.Aye(out)
  }

  def =\>>[I, O <: OUB](): I |-\- O = { _ =>
    system.Nay()
  }

  def =?>>[I, O <: OUB](): I |-?- O = { _ =>
    system.Abstain()
  }

  def =>><<=[I, O <: OUB](
      implicit
      proving: I |- O,
      refuting: I |-\- O
  ): I `_|_` O = { v =>
    system.Absurd(proving.consequentFor(v), refuting.consequentFor(v))
  }

  implicit def contradicting[I, O <: OUB](
      implicit
      proving: I |- O,
      refuting: I |-\- O
  ): `_|_`[I, O] = =>><<=[I, O]

  // equivalence, automatically implies O1 |- O2 & O2 |- O1
  case class <==>[O1 <: OUB, O2 <: OUB](
      forward: O1 |- O2,
      backward: O2 |- O1
  )

  object <==> {

    implicit def forward[O1 <: OUB, O2 <: OUB](
        implicit
        eq: O1 <==> O2
    ): O1 |- O2 = eq.forward

    implicit def backward[O1 <: OUB, O2 <: OUB](
        implicit
        eq: O1 <==> O2
    ): O2 |- O1 = eq.backward
  }

  trait SubScope extends system.ScopeInSystem {

    override type UpcastEvidence <: ProofScope.this.UpcastEvidence

    final override type System = ProofScope.this.System
    final override val system = ProofScope.this.system

    final val outer: ProofScope.this.type = ProofScope.this
  }

  {
    // sanity check, DO NOT DELETE!
    implicitly[System =:= SubScope#System]
    implicitly[Consequent =:= SubScope#Consequent]
  }

  type OffspringScope = system.ScopeInSystem {
    type UpcastEvidence <: ProofScope.this.UpcastEvidence

    type System = ProofScope.this.System
  }

  protected def coerciveUpcastImpl[
      I,
      P <: Consequent
  ](
      implicit
      inSubScope: OffspringScope#Proof[I, P]
  ): Proof[I, P] = { (v: I) =>
    inSubScope.consequentFor(v)
  }

  implicit def coerciveUpcast[
      I,
      P <: Consequent
  ](
      implicit
      inSubScope: OffspringScope#Proof[I, P]
  ): Proof[I, P] = { (v: I) =>
    inSubScope.consequentFor(v)
  }

  final def forAll[I]: ForAll[I, OUB] = new ForAll[I, OUB]
  protected class ForAll[I, OG <: OUB] {

    def =>>[O <: OG](_fn: I => O): I |- O = ProofScope.this.=>>(_fn)
    def =\>>[O <: OG](): I |-\- O = ProofScope.this.=\>>()
    def =?>>[O <: OG](): I |-?- O = ProofScope.this.=?>>()

    // summoners
    def prove[O <: OG](
        implicit
        theorem: I |- O
    ): I |- O = theorem

    def refute[O <: OG](
        implicit
        theorem: I |-\- O
    ): I |-\- O = theorem

    def ridicule[O <: OG](
        implicit
        contradiction: I `_|_` O
    ): I `_|_` O = contradiction

    def toGoal[O <: OUB] = new ForAll[I, O]

    def useTactic[O <: OG](
        tactic: Tactic.Empty[I, OG] => Tactic.Partial[I, O, OG]
    ): Tactic.Partial[I, O, OG] = {

      val empty = Tactic.Empty[I, OG]()
      val partial = tactic(empty)

      partial
    }
  }

  final def forTerm[I](v: I): ForTerm[I, OUB] = new ForTerm[I, OUB](v)
  class ForTerm[I, OG <: OUB](v: I) extends ForAll[I, OG] {

    def construct[O <: OG](
        implicit
        ev: I |- O
    ): O = ev.instanceFor(v)

    override def toGoal[O <: OUB] = new ForTerm[I, O](v)
  }
}

object ProofScope {

  type Aux[T] = ProofSystem {
    type OUB = T
  }
}
