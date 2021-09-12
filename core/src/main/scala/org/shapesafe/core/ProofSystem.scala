package org.shapesafe.core

import scala.language.implicitConversions

/**
  * This trait forms the backbone of compile-time reasoning and should reflect our best effort in reproducing
  * Curry-Howard isomorphism with scala compiler (regardless of how ill-suited it is), expecting drastic changes upon
  * improvement over its implicit search algorithm
  */
trait ProofSystem extends HasPropositions with ProofScope { // TODO: no IUB?

  final val system: this.type = this

  // constructive proof
  abstract class Proof[-I, +P <: Consequent] {

    def consequentFor(v: I): P

    final def instanceFor(v: I): P#Repr = {

      val verdict: P = consequentFor(v)
      verdict match {
        case y: Aye[_] =>
          y.asInstanceOf[Aye[P#Repr] with P].value // TODO: unsafe?
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
        lemma1: A |-< B,
        lemma2: B |-< C
    ) extends (A |- C) {

      override def consequentFor(v: A): system.Aye[C] = {

        system.Aye(
          lemma2.instanceFor(lemma1.instanceFor(v))
        )
      }
    }

//    implicit def hs[
//        A,
//        B <: OUB,
//        C <: OUB
//    ](
//        implicit
//        lemma1: A |-< B,
//        lemma2: B |-< C
//    ) = HS(lemma1, lemma2)
  }

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
}

object ProofSystem {

  type Aux[T] = ProofSystem {
    type OUB = T
  }

  trait ^[T] extends ProofSystem {
    final override type OUB = T
  }
}
