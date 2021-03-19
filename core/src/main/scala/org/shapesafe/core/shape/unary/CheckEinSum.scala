package org.shapesafe.core.shape.unary

import org.shapesafe.core.arity
import org.shapesafe.core.arity.ops.ArityOps
import org.shapesafe.core.shape.binary.OuterProduct
import org.shapesafe.core.shape.{Names, Shape}
import shapeless.ops.hlist.Reverse
import shapeless.{HList, SingletonProductArgs}

import scala.language.implicitConversions

// all names must be distinctive - no duplication allowed
case class CheckEinSum[
    S1 <: Shape
](
    override val s1: S1
) extends CheckEinSum.op._On[S1] {

  def ->[N <: Names](names: N): Reorder[CheckEinSum[S1], N] = {

    Reorder(this, names)
  }

  object ->* extends SingletonProductArgs {

    def applyProduct[H1 <: HList, H2 <: HList](
        v: H1
    )(
        implicit
        reverse: Reverse.Aux[H1, H2],
        lemma: Names.FromLiterals.Case[H2]
    ): Reorder[CheckEinSum[S1], lemma.Out] = {

      val names = lemma.apply(reverse(v))
      ->(names)
    }
  }

  def apply[S2 <: Shape](that: CheckEinSum[S2]): CheckEinSum[OuterProduct[CheckEinSum[S1], CheckEinSum[S2]]] = {
    val direct: OuterProduct[
      CheckEinSum[S1],
      CheckEinSum[S2]
    ] = this >< that

    CheckEinSum(direct)
  }

  def apply[S2 <: Shape](that: S2): CheckEinSum[OuterProduct[CheckEinSum[S1], CheckEinSum[S2]]] = {

    apply(CheckEinSum(that))
  }

  def einSum: this.type = this
}

object CheckEinSum {

  val op: arity.ops.ArityOps.:==!.AppendByName.type = ArityOps.:==!.AppendByName

  def indexing: op._Indexing.type = op._Indexing
}
