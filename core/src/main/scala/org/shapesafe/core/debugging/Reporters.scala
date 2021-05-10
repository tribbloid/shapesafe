package org.shapesafe.core.debugging

import org.shapesafe.core.debugging.OpStr.OpStr
import org.shapesafe.core.debugging.Expr.Expr
import org.shapesafe.core.{Poly1Base, ProofScope}
import org.shapesafe.m.viz.ExpressionVizCT
import org.shapesafe.m.viz.VizCTSystem.{EmitError, EmitInfo}
import singleton.ops.{+, XString}
import singleton.ops.impl.Op

// TODO: this weird abuse of implicit priority is due to the fact that
//  singleton-ops RequireMsg only cache the last message in the implicit search
//  so step 1 is isolated to avoid triggering RequireMsg prematurely
class Reporters[
    PS <: ProofScope
](val scope: PS) {

  import Reporters._

  trait ExprProofReporter[IUB <: CanPeek, TGT <: scope.OUB with CanPeek] extends Reporter[IUB] {

    import scope._
    import org.shapesafe.core.debugging.DebuggingUtil._

    trait Step1_Imp3 extends Poly1Base[Iub, XString] {

      implicit def raw[A <: Iub, VA <: XString](
          implicit
          vizA: ExpressionVizCT.NoTree.InfoOf.Aux[Expr[A], VA],
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
          vizA: ExpressionVizCT.NoTree.InfoOf.Aux[Expr[A], VA],
          vizS: ExpressionVizCT.NoTree.InfoOf.Aux[Expr[S], VS],
          mk: (PEEK.T + VS + EntailsLF + VA + "\n") { type Out <: XString }
      ): A ==> mk.Out =
        forAll[A].==>(_ => mk.value)
    }

    trait Step1_Imp1 extends Step1_Imp2 {

      implicit def alreadyTarget[
          S <: TGT with Iub,
          VS <: XString
      ](
          implicit
          vizS: ExpressionVizCT.NoTree.InfoOf.Aux[Expr[S], VS],
          op: (PEEK.T + VS + "\n") { type Out <: XString }
      ): S ==> op.Out =
        forAll[S].==>(_ => op.value)
    }

    override object Step1 extends Step1_Imp1
  }

  trait OpProofReporter[IUB <: CanPeek, TGT <: scope.OUB with CanPeek] extends Reporter[IUB] {

    import scope._
    import org.shapesafe.core.debugging.DebuggingUtil._

    trait Step1_Imp3 extends Poly1Base[Iub, XString] {

      implicit def raw[A <: Iub](
          implicit
          mk: (CannotEval + OpStr[A] + "\n") { type Out <: XString }
      ): A ==> mk.Out =
        forAll[A].==>(_ => mk.value)
    }

    trait Step1_Imp2 extends Step1_Imp3 {

      implicit def eval[
          A <: Iub,
          S <: TGT
      ](
          implicit
          lemma: A |- S,
          mk: (PEEK.T + OpStr[S] + EntailsLF + OpStr[A] + "\n") { type Out <: XString }
      ): A ==> mk.Out =
        forAll[A].==>(_ => mk.value)
    }

    trait Step1_Imp1 extends Step1_Imp2 {

      implicit def alreadyTarget[S <: TGT with Iub](
          implicit
          mk: (PEEK.T + OpStr[S] + "\n") { type Out <: XString }
      ): S ==> mk.Out =
        forAll[S].==>(_ => mk.value)
    }

    override object Step1 extends Step1_Imp1
  }

  trait PeekReporter[IUB <: CanPeek, TGT <: scope.OUB with CanPeek] extends ExprProofReporter[IUB, TGT] {

    override type EmitMsg[T] = EmitInfo[T]
  }

  trait InterruptReporter[IUB <: CanPeek, TGT <: scope.OUB with CanPeek] extends ExprProofReporter[IUB, TGT] {

    override type EmitMsg[T] = EmitError[T]
  }

}

object Reporters {

  trait Reporter[IUB] extends Poly1Base[IUB, Unit] {

    type EmitMsg[T] <: Op

    final type Iub = IUB

    val Step1: Poly1Base[IUB, XString]

    case class From[IN <: IUB](v: IN) {

      def getReportMsg[
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

//  trait MsgBroker {
//    type Out <: Op
//  }
//
//  object MsgBroker {
//
//    class ^[O <: Op] extends MsgBroker {
//      type Out = O
//    }
//
//    def apply[O <: Op]: ^[O] = new ^[O]
//  }
}
