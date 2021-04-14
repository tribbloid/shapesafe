package org.shapesafe.core.debugging

import org.shapesafe.core.debugging.InfoCT.{CanPeek, Peek, PeekMsg, YIELD}
import org.shapesafe.core.{Poly1Base, ProofScope}
import singleton.ops.+

// TODO: this weird abuse of implicit priority is due to the fact that
//  singleton-ops RequireMsg only cache the last message in the implicit search
//  so step 1 is isolated to avoid triggering RequireMsg prematurely
class Debuggers[
    SCOPE <: ProofScope
](val scope: SCOPE) {

  trait PeekFn[IUB <: CanPeek, TGT <: scope.OUB with CanPeek] extends Poly1Base[IUB, Unit] {

    implicit def attempt[
        A <: IUB,
        C <: MsgBroker
    ](
        implicit
        step1: Step1.Case.Aux[A, C],
        step2: PeekMsg[C#Out]
    ): A ==> Unit = forAll[A].==> { _ => }

    trait MsgBroker {
      type Out
    }

    object MsgBroker {

      type Aux[O] = MsgBroker { type Out = O }

      def apply[O]: Aux[O] = new MsgBroker {
        override type Out = O
      }
    }

    import MsgBroker._
    import scope._

    trait Step1_Imp3 extends Poly1Base[CanPeek, MsgBroker] {

      implicit def raw[A <: IUB]: A ==> Aux[Peek[A]] =
        forAll[A].==>(_ => MsgBroker[Peek[A]])
    }

    trait Step1_Imp2 extends Step1_Imp3 {

      implicit def eval[
          A <: IUB,
          S <: TGT
      ](
          implicit
          lemma: A |- S
      ): A ==> Aux[Peek[A] + YIELD.T + Peek[S]] =
        forAll[A].==>(_ => MsgBroker[Peek[A] + InfoCT.YIELD.T + Peek[S]])
    }

    trait Step1_Imp1 extends Step1_Imp2 {

      implicit def alreadyLeaf[S <: TGT]: S ==> Aux[Peek[S]] =
        forAll[S].==>(_ => MsgBroker[Peek[S]])
    }

    object Step1 extends Step1_Imp1
  }
}
