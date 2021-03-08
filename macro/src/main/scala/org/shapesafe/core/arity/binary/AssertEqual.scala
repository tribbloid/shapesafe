package org.shapesafe.core.arity.binary

import com.tribbloids.graph.commons.util.HasOuter
import org.shapesafe.core.arity.LeafArity.Const
import org.shapesafe.core.arity.ProveArity.{|-<, Proof}
import org.shapesafe.core.arity.ops.ArityOps.=!=
import org.shapesafe.core.arity.{Arity, ProveArity}
import shapeless.Witness

trait AssertEqual_Imp0 {

  implicit def unchecked[
      A1 <: Arity,
      A2 <: Arity,
      O <: ProveArity.Term
  ](
      implicit
      domain: UncheckedDomain[A1, A2, O]
  ): AssertEqual.On[A1, A2] Proof O = {
    domain.forEqual
  }
}

object AssertEqual extends AssertEqual_Imp0 with Op2Like {

  import ProveArity.Factory._
  import singleton.ops._

  case class On[
      +A1 <: Arity,
      +A2 <: Arity
  ](
      a1: A1,
      a2: A2
  ) extends Arity
      with HasOuter {

    def outer: AssertEqual.type = AssertEqual.this

    override lazy val runtimeArity: Int = {
      val v1 = a1.runtimeArity
      val v2 = a2.runtimeArity

      require(v1 == v2)
      v1
    }
  }

  val msgInfix = Witness(" == ")
  override type MsgInfix = msgInfix.T

  implicit def invar[
      A1 <: Arity,
      A2 <: Arity,
      S1,
      S2
  ](
      implicit
      bound1: A1 |-< Const[S1],
      bound2: A2 |-< Const[S2],
      //      lemma: Require[S1 == S2]
      lemma: RequireMsg[
        S1 == S2,
        MsgTitle + Msg.Infix[ToString[S1], ToString[S2]]
      ]
//      lemma: RequireMsgSym[S1 == S2, noLemmaMsg.T, A1 =!= A2]
  ): A1 =!= A2 =>> Const[S1] = {

    val domain = InvarDomain[A1, A2, S1, S2]()(bound1, bound2)

    domain.forEqual(lemma)
  }

  override def on[
      A1 <: Arity,
      A2 <: Arity
  ](a1: A1, a2: A2): On[A1, A2] = {
    On(a1, a2)
  }

}
