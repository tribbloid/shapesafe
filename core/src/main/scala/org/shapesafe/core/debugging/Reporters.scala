package org.shapesafe.core.debugging

import org.shapesafe.core.debugging.DebugUtil.{CanRefute, Refute, Stripe}
import org.shapesafe.core.debugging.Expressions.Expr
import org.shapesafe.core.debugging.OpStrs.OpStr
import org.shapesafe.core.{Poly1Base, ProofScope}
import org.shapesafe.m.viz.ExpressionVizCT
import org.shapesafe.m.viz.VizCTSystem.{EmitError, EmitInfo}
import singleton.ops.{+, XString}

// TODO: this weird abuse of implicit priority is due to the fact that
//  singleton-ops RequireMsg only cache the last message in the implicit search
//  so step 1 is isolated to avoid triggering RequireMsg prematurely
class Reporters[
    PS <: ProofScope
](val scope: PS) {

  import Reporters._

  trait ExprProofReporter[IUB <: CanPeek, TGT <: scope.OUB with CanPeek] extends Reporter[IUB] {

    import org.shapesafe.core.debugging.DebugUtil._
    import scope._

    trait Step1_Imp3 extends Poly1Base[Iub, XString] {

      implicit def raw[A <: Iub, VA <: XString](
          implicit
          vizA: Expr2Str[Expr[A], VA],
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
          vizA: Expr2Str[Expr[A], VA],
          vizS: Expr2Str[Expr[S], VS],
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
          vizS: Expr2Str[Expr[S], VS],
          op: (PEEK.T + VS + "\n") { type Out <: XString }
      ): S ==> op.Out =
        forAll[S].==>(_ => op.value)
    }

    override object Step1 extends Step1_Imp1
  }

  trait OpProofReporter[IUB <: CanPeek, TGT <: scope.OUB with CanPeek] extends Reporter[IUB] {

    import org.shapesafe.core.debugging.DebugUtil._
    import scope._

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

  type Expr2Str[I, O <: String] = ExpressionVizCT.NoTree.InfoOf.Aux[I, O]

  trait Reporter[IUB] extends Poly1Base[IUB, Unit] {

    type EmitMsg[T]

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

  trait Refutes {

    type TryStripe

    // TODO: remove, obsolete design
//    type Refute0[SELF <: CanPeek with CanRefute] =
//      Refute[SELF] +
//        TryStripe +
//        OpStr[SELF]

    type Refute0[SELF <: CanPeek with CanRefute, O] = Refute0.Case.Aux[SELF, O]

    object Refute0 extends Poly1Base[CanPeek with CanRefute, Any] {

      implicit def get[I <: _IUB, V <: String](
          implicit
          expr2Str: Reporters.Expr2Str[I#_AsExpr, V]
      ): I ==> (
        Refute[I] +
          TryStripe +
          V
      ) = forAll[I].==> { _ =>
        null
      }

//      Dimension mismatch
//
//      ... when proving shape ░▒▓
//
//      1 >< 2 >< 3 >< 4 |<<- (i >< j)
    }
  }

  object ForArity extends Refutes {

    type TryStripe = "\n\n" + Stripe["... when proving arity"]

    //    type Refute1[SELF <: CanPeek with CanRefute, C1] =
    //      Refute[SELF] +
    //        TryArity +
    //        OpStr[SELF] +
    //        FROM1.T +
    //        C1
    //
    //    type Refute2[SELF <: CanPeek with CanRefute, C1, C2] =
    //      OpStr[SELF] +
    //        TryArity +
    //        Refute[SELF] +
    //        FROM2.T +
    //        C1 +
    //        "\n\n" +
    //        C2
  }

  object ForShape extends Refutes {

    type TryStripe = "\n\n" + Stripe["... when proving shape"]

  }
}
