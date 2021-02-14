package org.shapesafe.core.arity.binary

import org.shapesafe.core.arity.ProveArity.~~>
import org.shapesafe.core.arity.Utils.Op
import org.shapesafe.core.arity.{Arity, ProveArity, Utils}

import scala.language.{higherKinds, implicitConversions}

class Op2[
    ??[X1, X2] <: Op
](
    implicit
    val tfs: Utils.IntSh[??]
) extends Op2Like {

  override def apply[
      A1 <: Arity,
      A2 <: Arity
  ](
      a1: A1,
      a2: A2
  ): On[A1, A2] = On(a1, a2)

  case class On[
      +A1 <: Arity,
      +A2 <: Arity
  ](
      a1: A1,
      a2: A2
  ) extends Arity {

    override lazy val number: Int = tfs.apply(a1.number, a2.number).getValue
  }
}

trait Op2_Imp0 {

  implicit def unsafe[
      A1 <: Arity,
      A2 <: Arity,
      O <: ProveArity.Proof,
      ??[X1, X2] <: Op
  ](
      implicit
      domain: UnknownDomain[A1, A2, O],
      tfs: Utils.IntSh[??]
  ): domain.ForOp2[??] = {
    domain.ForOp2[??]()
  }
}

object Op2 extends Op2_Imp0 {

  implicit def invar[
      A1 <: Arity,
      A2 <: Arity,
      S1,
      S2,
      ??[X1, X2] <: Op
  ](
      implicit
      bound1: A1 ~~> ProveArity.OfStaticImpl[S1], // TODO: make it similar to unsafe
      bound2: A2 ~~> ProveArity.OfStaticImpl[S2],
      lemma: S1 ?? S2
  ): InvarDomain[A1, A2, S1, S2]#ForOp2[??] = {
    val domain = InvarDomain[A1, A2, S1, S2]()(bound1, bound2)

    domain.ForOp2[??]()(lemma)
  }

  def apply[
      ??[X1, X2] <: Op,
      A1 <: Arity,
      A2 <: Arity
  ](
      a1: A1,
      a2: A2
  )(
      implicit
      tfs: Utils.IntSh[??]
  ): Op2[??]#On[A1, A2] = {

    val op2 = new Op2[??] // TODO: should be cached

    op2.On(a1, a2)
  }
}
