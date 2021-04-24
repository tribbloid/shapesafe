package org.shapesafe.core.debugging

import org.shapesafe.core.debugging.InfoCT.{PEEK, _}
import org.shapesafe.core.{Poly1Base, ProofScope}
import singleton.ops.impl.{Op, OpString}
import singleton.ops.{+, ToString}

// TODO: this weird abuse of implicit priority is due to the fact that
//  singleton-ops RequireMsg only cache the last message in the implicit search
//  so step 1 is isolated to avoid triggering RequireMsg prematurely
class Reporters[
    PS <: ProofScope
](val scope: PS) {

  import Reporters._

  trait ProofReporter[IUB <: CanPeek, TGT <: scope.OUB with CanPeek] extends Reporter[IUB] {

    import scope._

    trait Step1_Imp3 extends Poly1Base[Iub, MsgBroker] {

      implicit def raw[A <: Iub]: A ==> Reporters.MsgBroker.^[CannotEval + Peek[A] + "\n"] =
        forAll[A].==>(_ => MsgBroker[CannotEval + Peek[A] + "\n"])
    }

    trait Step1_Imp2 extends Step1_Imp3 {

      implicit def eval[
          A <: Iub,
          S <: TGT
      ](
          implicit
          lemma: A |- S
      ): A ==> Reporters.MsgBroker.^[PEEK.T + Peek[S] + EntailsLF + Peek[A] + "\n"] =
        forAll[A].==>(_ => MsgBroker[InfoCT.PEEK.T + Peek[S] + EntailsLF + Peek[A] + "\n"])
    }

    trait Step1_Imp1 extends Step1_Imp2 {

      implicit def alreadyTarget[S <: TGT with Iub]: S ==> Reporters.MsgBroker.^[PEEK.T + Peek[S] + "\n"] =
        forAll[S].==>(_ => MsgBroker[InfoCT.PEEK.T + Peek[S] + "\n"])
    }

    override object Step1 extends Step1_Imp1
  }

  trait PeekReporter[IUB <: CanPeek, TGT <: scope.OUB with CanPeek] extends ProofReporter[IUB, TGT] {

    override type ReportMsg[T] = WarnMsg[T]
  }

  trait ErrorReporter[IUB <: CanPeek, TGT <: scope.OUB with CanPeek] extends ProofReporter[IUB, TGT] {

    override type ReportMsg[T] = ErrorMsg[T]
  }

}

object Reporters {

  trait Reporter[IUB] extends Poly1Base[IUB, Unit] {

    type ReportMsg[T] <: Op

    final type Iub = IUB

    val Step1: Poly1Base[IUB, MsgBroker]

    case class From[IN <: IUB]() {

      def getReportMsg[
          MSG <: MsgBroker,
          SS <: String with Singleton
      ](
          implicit
          step1: Step1.Case.Aux[IN, MSG],
          toString: OpString.Aux[ToString[MSG#Out], SS]
      ): SS = {

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

    class ^[O] extends MsgBroker {
      type Out = O
    }

    def apply[O]: ^[O] = new ^[O]
  }
}
