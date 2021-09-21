package org.shapesafe.core

import scala.annotation.implicitNotFound
import scala.language.implicitConversions

trait ProofScope { // TODO: no IUB?

  type OUB

  val system: ProofSystem.Aux[OUB]

  final type Consequent = system.Proposition

  // TODO: this should potentially be merged with Refutation cases in MsgBroker
  //  Such that successful proof can show reasoning at runtime.
  //  At this moment, this feature is implemented in PeekReporter
  //  which is too complex for its own good

  // constructive proof
  abstract class Proof[-I, +P <: system.Consequent] {
//  abstract class Proof[-I, +P <: Consequent] { // why this doesn't work?

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

    implicit def coerciveUpcast[I, P <: system.Proposition](
        implicit
        proofInSubScope: SubScope#Proof[I, P]
    ): Proof[I, P] = { (v: I) =>
      proofInSubScope.consequentFor(v)
    }

    case class Chain[ // a.k.a hypothetical syllogism
        A,
        B <: OUB,
        C <: OUB
    ](
        lemma1: A |-< B,
        lemma2: B |-< C
    ) extends (A |- C) {

      override def consequentFor(v: A): system.Aye[C] = {

        system.Aye(
          lemma2.instanceFor(lemma1.instanceFor(v))
        )
      }
    }

    //    implicit def chain[
    //        A,
    //        B <: OUB,
    //        C <: OUB
    //    ](
    //        implicit
    //        lemma1: A |-< B,
    //        lemma2: B |-< C
    //    ) = Chain(lemma1, lemma2)
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

  final type |-?-[-I, O <: OUB] = Proof[I, system.Grey[O]]

  final type `_|_`[-I, O <: OUB] = Proof[I, system.Absurd[O]]

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
    system.Grey()
  }

  implicit def =>><<=[I, O <: OUB](
      implicit
      proving: I |- O,
      refuting: I |-\- O
  ): I `_|_` O = { v =>
    system.Absurd(proving.consequentFor(v), refuting.consequentFor(v))
  }

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
        eq: O1 <==> O2
    ): O2 |- O1 = eq.backward
  }

  abstract class SubScope extends ProofScope {

    final val outer: ProofScope.this.type = ProofScope.this
    final override val system: ProofScope.this.system.type = ProofScope.this.system

    final override type OUB = outer.OUB

    //    override def fromFn[I, O <: OUB](_fn: I => O): Proof[I, system.Aye[O]]
  }

  trait TacticalStage[I, SUBG, OG <: OUB] {

    def toGoal[O <: OUB]: TacticalStage[I, SUBG, O]

    def citing[_SUBG <: OG](lemma: SUBG |- _SUBG): TacticalStage[I, _SUBG, OG]

    type SubGoal = SUBG
    type OutputGoal = OG
  }

  final def forAll[I]: ForAll[I, OUB] = new ForAll[I, OUB]
  protected class ForAll[I, OG <: OUB] extends TacticalStage[I, I, OG] {

    def =>>[O <: OG](_fn: I => O): I |- O = ProofScope.this.=>>(_fn)
    def =\>>[O <: OG](): I |-\- O = ProofScope.this.=\>>()
    def =?>>[O <: OG](): I |-?- O = ProofScope.this.=?>>()

    // summoners
    def prove[O <: OG](
        implicit
        ev: I |- O
    ): I |- O = ev

    def refute[O <: OG](
        implicit
        ev: I |-\- O
    ): I |-\- O = ev

    def ridicule[O <: OG](
        implicit
        ev: I `_|_` O
    ): `_|_`[I, O] = ev

    def toGoal[O <: OUB] = new ForAll[I, O]

    // tactic mode! https://leanprover-community.github.io/extras/conv.html

    override def citing[O <: OG](lemma: I |- O): ProofBuilder[I, O, OG] = {

      ProofBuilder(lemma)
    }
  }

  // tactic mode!
  case class ProofBuilder[I, SUBG <: OUB, OG <: OUB](
      antecedent: I |- SUBG
  ) extends TacticalStage[I, SUBG, OG] {

    override def toGoal[O <: OUB]: ProofBuilder[I, SUBG, O] = {
      ProofBuilder[I, SUBG, O](antecedent)
    }

    override def citing[O <: OG](lemma: SUBG |- O): ProofBuilder[I, O, OG] = {

      ProofBuilder(
        Proof.Chain[I, SUBG, O](antecedent, lemma)
      )
    }

    def build(
        implicit
        canUpcast: SUBG <:< OG
    ): I |- OG = antecedent.asInstanceOf[I |- OG]
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

object ProofScope {}
