package org.shapesafe.core

import scala.language.implicitConversions

/**
  * This trait forms the backbone of compile-time reasoning and should reflect our best effort in reproducing
  * Curry-Howard isomorphism with scala compiler (regardless of how ill-suited it is), expecting drastic changes upon
  * improvement over its implicit search algorithm
  * @tparam _OUB upper bound of output
  */
trait ProverSystem[_OUB] extends HasPropositions[_OUB] with ProverScope { // TODO: no IUB?

  final override type OUB = _OUB

  final val system: this.type = this

  abstract class Proof[-I, +P <: Consequent] {

    def apply(v: I): P

    // constructive proof
    final def valueOf(v: I): P#Repr = {

      val verdict: P = apply(v)
      verdict match {
        case y: Aye[_] =>
          y.asInstanceOf[Aye[P#Repr] with P].value // TODO: unsafe?
        case _ =>
          throw new UnsupportedOperationException(
            s"$verdict is not a constructive proposition"
          )
      }
    }
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

  implicit def adAbsurdum[I, O <: OUB](
      implicit
      proving: I |- O,
      refuting: I |-\- O
  ): I `_|_` O = { v =>
    system.Absurd(proving(v), refuting(v))
  }
}

object ProverSystem {}
