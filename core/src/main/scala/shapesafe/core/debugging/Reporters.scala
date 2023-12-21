package shapesafe.core.debugging

import shapesafe.core.AdHocPoly1
import shapesafe.core.logic.HasTheory
import shapesafe.m.Emit
import shapesafe.m.viz.PeekCT
import singleton.ops.{+, XString}

// TODO: this weird abuse of implicit priority is due to the fact that
//  singleton-ops RequireMsg only cache the last message in the implicit search
//  so step 1 is isolated to avoid triggering RequireMsg prematurely
trait Reporters extends HasTheory {

  import Reporters._

  trait ProofReporter[IUB <: CanPeek, TGT <: CanPeek] extends Reporter {

    import shapesafe.core.debugging.DebugConst._
    import theory._

    type ExprOf[T <: CanPeek] = T#Notation

    trait Step1_Imp3 extends AdHocPoly1 {

      implicit def raw[A <: CanPeek, VA <: XString](
          implicit
          vizA: PeekCTAux[A#Notation, VA],
          mk: (CannotEval + VA + "\n") { type Out <: XString }
      ): A =>> mk.Out =
        forAll[A].=>>(_ => mk.value)
    }

    trait Step1_Imp2 extends Step1_Imp3 {

      implicit def eval[
          A <: CanPeek,
          S <: TGT,
          VA <: XString,
          VS <: XString
      ](
          implicit
          lemma: A |- S,
          vizA: PeekCTAux[A#Notation, VA],
          vizS: PeekCTAux[ExprOf[S], VS],
          mk: (PEEK.T + VS + EquivLF + VA + "\n") { type Out <: XString }
      ): A =>> mk.Out =
        forAll[A].=>>(_ => mk.value)
    }

    trait Step1_Imp1 extends Step1_Imp2 {

      implicit def alreadyEvaled[
          S <: TGT with CanPeek,
          VS <: XString
      ](
          implicit
          vizS: PeekCTAux[ExprOf[S], VS],
          op: (PEEK.T + VS + "\n") { type Out <: XString }
      ): S =>> op.Out =
        forAll[S].=>>(_ => op.value)
    }

    override object Step1 extends Step1_Imp1
  }

  trait PeekReporter[IUB <: CanPeek, TGT <: CanPeek] extends ProofReporter[IUB, TGT] {

    override type EmitMsg[T] = Emit.Info[T]
  }

  trait InterruptReporter[IUB <: CanPeek, TGT <: CanPeek] extends ProofReporter[IUB, TGT] {

    override type EmitMsg[T] = Emit.Error[T]
  }
}

object Reporters {

  type PeekCTAux[I, O <: String] = PeekCT.NoTree.Info.Aux[I, O]

  trait Reporter extends AdHocPoly1 {

    type EmitMsg[T]

    val Step1: AdHocPoly1

    case class ForTerm[IN](v: IN) {

      // TODO: should this be part of Arity/Shape API?
      def getMessage[
          SS <: XString
      ](
          implicit
          step1: Step1.Auxs.=>>[IN, SS]
      ): SS = {

        step1.apply(v)
      }
    }

    implicit def attempt[
        IN,
        SS <: XString
    ](
        implicit
        step1: Step1.Auxs.=>>[IN, SS],
        step2: EmitMsg[SS]
    ): IN =>> Unit = forAll[IN].=>> { _ =>
      //      val emit = new EmitMsg[SS, EmitMsg.Error]
      //      emit.emit
    }
  }

}
