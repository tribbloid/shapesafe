package org.shapesafe.core.debugging

import org.shapesafe.core.debugging.DebugUtil.{Refute, Stripe}
import org.shapesafe.core.Poly1Base
import org.shapesafe.core.logic.Theory
import org.shapesafe.m.viz.PeekCT
import org.shapesafe.m.viz.VizCTSystem.{EmitError, EmitInfo}
import singleton.ops.{+, XString}

// TODO: this weird abuse of implicit priority is due to the fact that
//  singleton-ops RequireMsg only cache the last message in the implicit search
//  so step 1 is isolated to avoid triggering RequireMsg prematurely
class Reporters[
    PS <: Theory
](val scope: PS) {

  import Reporters._

  trait ProofReporter[IUB <: CanPeek, TGT <: scope.OUB with CanPeek] extends Reporter[IUB] {

    import org.shapesafe.core.debugging.DebugUtil._
    import scope._

    type ExprOf[T <: CanPeek] = T#Expr

    trait Step1_Imp3 extends Poly1Base[Iub, XString] {

      implicit def raw[A <: Iub, VA <: XString](
          implicit
          vizA: PeekCTAux[A#Expr, VA],
          mk: (CannotEval + VA + "\n") { type Out <: XString }
      ): A ==> mk.Out =
        forAll[A].==>(_ => mk.value)
    }

    trait Step1_Imp2 extends Step1_Imp3 {

      implicit def eval[
          A <: Iub,
          S <: TGT,
          VA <: XString,
          VS <: XString
      ](
          implicit
          lemma: A |- S,
          vizA: PeekCTAux[A#Expr, VA],
          vizS: PeekCTAux[ExprOf[S], VS],
          mk: (PEEK.T + VS + EquivLF + VA + "\n") { type Out <: XString }
      ): A ==> mk.Out =
        forAll[A].==>(_ => mk.value)
    }

    trait Step1_Imp1 extends Step1_Imp2 {

      implicit def alreadyEvaled[
          S <: TGT with Iub,
          VS <: XString
      ](
          implicit
          vizS: PeekCTAux[ExprOf[S], VS],
          op: (PEEK.T + VS + "\n") { type Out <: XString }
      ): S ==> op.Out =
        forAll[S].==>(_ => op.value)
    }

    override object Step1 extends Step1_Imp1
  }

  trait PeekReporter[IUB <: CanPeek, TGT <: scope.OUB with CanPeek] extends ProofReporter[IUB, TGT] {

    override type EmitMsg[T] = EmitInfo[T]
  }

  trait InterruptReporter[IUB <: CanPeek, TGT <: scope.OUB with CanPeek] extends ProofReporter[IUB, TGT] {

    override type EmitMsg[T] = EmitError[T]
  }
}

object Reporters {

  type PeekCTAux[I, O <: String] = PeekCT.NoTree.Info.Aux[I, O]

  trait Reporter[IUB] extends Poly1Base[IUB, Unit] {

    type EmitMsg[T]

    final type Iub = IUB

    val Step1: Poly1Base[IUB, XString]

    case class ForTerm[IN <: IUB](v: IN) {

      // TODO: should this be part of Arity/Shape API?
      def getMessage[
          SS <: XString
      ](
          implicit
          step1: Step1.Case.Aux[IN, SS]
      ): SS = {

        step1.apply(v)
      }
    }

    implicit def attempt[
        IN <: IUB,
        SS <: XString
    ](
        implicit
        step1: Step1.Case.Aux[IN, SS],
        step2: EmitMsg[SS]
    ): IN ==> Unit = forAll[IN].==> { _ =>
      //      val emit = new EmitMsg[SS, EmitMsg.Error]
      //      emit.emit
    }
  }

  trait Refutes {

    type TryStripe

    type Refute0[SELF <: CanPeek with CanRefute, O] = Refute0.Case.Aux[SELF, O]

    object Refute0 extends Poly1Base[CanPeek with CanRefute, Any] {

      implicit def get[I <: _IUB, V <: String](
          implicit
          expr2Str: PeekCTAux[I#Expr, V]
      ): I ==> (
        Refute[I] +
          TryStripe +
          V
      ) = forAll[I].==> { _ =>
        null
      }
    }
  }

  object ForArity extends Refutes {

    type TryStripe = "\n\n" + Stripe["... when proving arity"]

  }

  object ForShape extends Refutes {

    type TryStripe = "\n\n" + Stripe["... when proving shape"]

  }
}
