package org.shapesafe.core.debugging

import org.shapesafe.core.debugging.InfoCT.{PEEK, _}
import org.shapesafe.core.{Poly1Base, ProofScope}
import singleton.ops.impl.{Op, OpString}
import singleton.ops.{+, RequireMsg, ToString}

// TODO: this weird abuse of implicit priority is due to the fact that
//  singleton-ops RequireMsg only cache the last message in the implicit search
//  so step 1 is isolated to avoid triggering RequireMsg prematurely
class Reporters[
    PS <: ProofScope
](val scope: PS) {

  import Reporters._

  trait ProofReporter[IUB <: CanPeek, TGT <: scope.OUB with CanPeek] extends ReporterBase[IUB] {

    import scope._

    trait Step1_Imp3 extends Poly1Base[Iub, MsgBroker] {

      implicit def raw[A <: Iub] =
        forAll[A].==>(_ => MsgBroker.apply[CannotEval + Peek[A] + "\n"])
    }

    trait Step1_Imp2 extends Step1_Imp3 {

      implicit def eval[
          A <: Iub,
          S <: TGT
      ](
          implicit
          lemma: A |- S
      ) =
        forAll[A].==>(_ => MsgBroker.apply[InfoCT.PEEK.T + Peek[S] + EntailsLF + Peek[A] + "\n"])
    }

    trait Step1_Imp1 extends Step1_Imp2 {

      implicit def alreadyPreferred[S <: TGT with Iub] =
        forAll[S].==>(_ => MsgBroker.apply[InfoCT.PEEK.T + Peek[S] + "\n"])
    }

    override object Step1 extends Step1_Imp1
  }

  trait PeekReporter[IUB <: CanPeek, TGT <: scope.OUB with CanPeek] extends ProofReporter[IUB, TGT] {

    override type ReportMsg[T] = WarnMsg[T]
  }

  trait ErrorReporter[IUB <: CanPeek, TGT <: scope.OUB with CanPeek] extends ProofReporter[IUB, TGT] {

    override type ReportMsg[T] = ErrorMsg[T]
  }

//  trait RefuteReporter[IUB <: CanRefute] extends ReporterBase[IUB] {
//
//    override type ReportMsg[T] = RequireMsgSym[FALSE.T, T, Warn]
//  }
}

object Reporters {

  trait ReporterBase[IUB] extends Poly1Base[IUB, Unit] {

    type ReportMsg[T] <: Op

    final type Iub = IUB

    val Step1: Poly1Base[IUB, MsgBroker]

    object getReportMsg

    case class From[IN <: IUB]() {

      def getReportMsg[
          MSG <: MsgBroker,
          SS <: String with Singleton
      ](
          implicit
          step1: Step1.Case.Aux[IN, MSG],
          toString: OpString.Aux[ToString[MSG#Out], SS]
      ): SS = {

//        step1.apply(null.asInstanceOf[IN])
        toString.value
      }
    }

    implicit def attempt[
        IN <: IUB,
        MSG <: MsgBroker
    ](
        implicit
        step1: Step1.Case.Aux[IN, MSG],
        step2: ReportMsg[MSG#Out]
    ): IN ==> Unit = forAll[IN].==> { _ => }
  }

  trait MsgBroker {
    type Out
  }

  object MsgBroker {

    type Aux[O] = MsgBroker { type Out = O }

    def apply[O]: Aux[O] = new MsgBroker {
      override type Out = O
    }

  }
}
