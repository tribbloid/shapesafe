package org.shapesafe.core.shape

import org.shapesafe.core.tuple.{CanInfix_><, StaticTuples, TupleSystem}

import scala.language.implicitConversions

trait Indices extends IndicesMagnet with Indices.Proto.Tuple {

  final override type AsIndices = this.type

  final override def asIndices: Indices.this.type = this
}

object Indices extends CanInfix_>< {

  type VBound = Index

  object Proto extends StaticTuples.Total[VBound] with CanInfix_>< {}

  type Tuple = Indices

  class Eye extends Proto.Eye with Indices
  lazy val Eye = new Eye

  class ><[
      TAIL <: Indices,
      HEAD <: VBound
  ](
      override val tail: TAIL,
      override val head: HEAD
  ) extends Proto.><[TAIL, HEAD](tail, head)
      with Tuple {

    override type PeekHead = Head
  }

  implicit def consAlways[TAIL <: Tuple, HEAD <: VBound] = {

    ConsLemma.from[TAIL, HEAD].to { (tail, head) =>
      new ><(tail, head)
    }
  }

}
