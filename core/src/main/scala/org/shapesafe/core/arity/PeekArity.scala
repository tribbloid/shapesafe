package org.shapesafe.core.arity

import org.shapesafe.core.Poly1Base
import org.shapesafe.core.arity.ProveArity.|-
import org.shapesafe.core.debugging.InfoCT
import org.shapesafe.core.debugging.InfoCT.{PeekMsg, YIELD}
import singleton.ops.+

// TODO: this weird abuse of implicit priority is due to the fact that
//  singleton-ops RequireMsg only cache the last message in the implicit search
//  so step 1 is isolated to avoid triggering RequireMsg prematurely
object PeekArity extends Poly1Base[Arity, Unit] {

  implicit def doIt[
      A <: Arity,
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

  trait Step1_Imp3 extends Poly1Base[Arity, MsgBroker] {

    implicit def raw[A <: Arity]: A ==> Aux[A#Peek] =
      forAll[A].==>(_ => MsgBroker[A#Peek])
  }

  trait Step1_Imp2 extends Step1_Imp3 {

    implicit def eval[
        A <: Arity,
        S <: LeafArity
    ](
        implicit
        lemma: A |- S
    ): A ==> Aux[A#Peek + YIELD.T + S#Peek] =
      forAll[A].==>(_ => MsgBroker[A#Peek + InfoCT.YIELD.T + S#Peek])
  }

  trait Step1_Imp1 extends Step1_Imp2 {

    implicit def alreadyLeaf[S <: LeafArity]: S ==> Aux[S#Peek] =
      forAll[S].==>(_ => MsgBroker[S#Peek])
  }

  object Step1 extends Step1_Imp1
}
