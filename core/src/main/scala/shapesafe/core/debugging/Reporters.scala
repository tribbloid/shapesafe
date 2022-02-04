package shapesafe.core.debugging

import shapesafe.core.debugging.DebugConst.Stripe
import shapesafe.core.Poly1Base
import shapesafe.core.logic.Theory
import shapesafe.m.viz.PeekCT
import shapesafe.m.viz.VizCTSystem.{EmitError, EmitInfo}
import singleton.ops.{+, XString}

// TODO: this weird abuse of implicit priority is due to the fact that
//  singleton-ops RequireMsg only cache the last message in the implicit search
//  so step 1 is isolated to avoid triggering RequireMsg prematurely
class Reporters[
    PS <: Theory
](val scope: PS) {

  import Reporters._

  trait ProofReporter[IUB <: CanPeek, TGT <: CanPeek] extends Reporter[IUB] {

    import shapesafe.core.debugging.DebugConst._
    import scope._

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

    override type EmitMsg[T] = EmitInfo[T]
  }

  trait InterruptReporter[IUB <: CanPeek, TGT <: CanPeek] extends ProofReporter[IUB, TGT] {

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

  trait Refutes {

    type WHEN_PROVING

    type Refute0[SELF <: CanRefute, O] = Refute0.Auxs.=>>[SELF, O]

    object Refute0 extends Poly1Base[CanRefute, Any] {

      implicit def get[I <: _IUB, V <: String](
          implicit
          expr2Str: PeekCTAux[I#Notation, V]
      ): I =>> (
        I#RefuteTxt +
          WHEN_PROVING +
          V
      ) = forAll[I].=>> { _ =>
        null
      }
    }

    class NotFoundInfo[T, R <: CanRefute]

    trait NotFoundInfo_Imp0 {

      implicit def refute[
          T,
          R <: CanRefute,
          MSG
      ](
          implicit
          refute0: Refute0[R, MSG],
          msg: EmitError[MSG]
      ): NotFoundInfo[T, R] =
        ???
    }

    object NotFoundInfo extends NotFoundInfo_Imp0 {

      implicit def prove[
          T,
          R <: CanRefute
      ](
          implicit
          iff: T
      ): NotFoundInfo[T, R] = {
        new NotFoundInfo[T, R]
      }
    }
  }

  object ForArity extends Refutes {

    type WHEN_PROVING = "\n\n" + Stripe["... when proving arity"]
  }

  object ForShape extends Refutes {

    type WHEN_PROVING = "\n\n" + Stripe["... when proving shape"]
  }
}
