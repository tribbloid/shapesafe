package org.shapesafe.core.shape

import org.shapesafe.core.tuple.{CanInfix_><, StaticTuples, TupleSystem}

import scala.language.implicitConversions

trait Indices extends IndicesMagnet with Indices.proto.Tuple {

  final override type AsIndices = this.type

  final override def asIndices: Indices.this.type = this
}

object Indices extends TupleSystem with CanInfix_>< {

  type UpperBound = Index

  object proto extends StaticTuples.Total[UpperBound] with CanInfix_>< {}

  type Tuple = Indices

  class Eye extends proto.Eye with Indices
  lazy val Eye = new Eye

  class ><[
      TAIL <: Indices,
      HEAD <: UpperBound
  ](
      override val tail: TAIL,
      override val head: HEAD
  ) extends proto.><[TAIL, HEAD](tail, head)
      with Tuple {

    override type PeekHead = Head
  }

  implicit def consAlways[TAIL <: Tuple, HEAD <: UpperBound] = {

    Cons.from[TAIL, HEAD].to { (tail, head) =>
      new ><(tail, head)
    }
  }

}
