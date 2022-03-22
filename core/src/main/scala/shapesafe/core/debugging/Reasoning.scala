package shapesafe.core.debugging

import shapesafe.core.Poly1Base
import shapesafe.core.logic.Theory
import shapesafe.m.Emit
import shapesafe.m.viz.PeekCT
import singleton.ops.{+, XString}

// TODO: this weird abuse of implicit priority is due to the fact that
//  singleton-ops RequireMsg only cache the last message in the implicit search
//  so step 1 is isolated to avoid triggering RequireMsg prematurely
class Reasoning[
    PS <: Theory
](val theory: PS) {

  import Reasoning._

  trait ProofReporter[IUB <: CanPeek, TGT <: CanPeek] extends Reporter[IUB] {

    import shapesafe.core.debugging.DebugConst._
    import theory._

    type ExprOf[T <: CanPeek] = T#Notation

    trait Step1_Imp3 extends Poly1Base[Iub, XString] {

      implicit def raw[A <: Iub, VA <: XString](
          implicit
          vizA: PeekCTAux[A#Notation, VA],
          mk: (CannotEval + VA + "\n") { type Out <: XString }
      ): A =>> mk.Out =
        forAll[A].=>>(_ => mk.value)
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
          vizA: PeekCTAux[A#Notation, VA],
          vizS: PeekCTAux[ExprOf[S], VS],
          mk: (PEEK.T + VS + EquivLF + VA + "\n") { type Out <: XString }
      ): A =>> mk.Out =
        forAll[A].=>>(_ => mk.value)
    }

    trait Step1_Imp1 extends Step1_Imp2 {

      implicit def alreadyEvaled[
          S <: TGT with Iub,
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

object Reasoning {

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
          step1: Step1.Auxs.=>>[IN, SS]
      ): SS = {

        step1.apply(v)
      }
    }

    implicit def attempt[
        IN <: IUB,
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