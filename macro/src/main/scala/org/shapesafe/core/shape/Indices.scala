package org.shapesafe.core.shape

import org.shapesafe.core.tuple.{CanInfix_><, StaticTuples, TupleSystem}

import scala.language.implicitConversions

trait Indices extends IndicesMagnet with Indices.proto.Impl {

  final override type AsIndices = this.type

  final override def asIndices: Indices.this.type = this
}

object Indices extends TupleSystem with CanInfix_>< {

  object proto extends StaticTuples.Total[Index] with CanInfix_>< {}

  type Impl = Indices
  type UpperBound = proto.UpperBound

  class Eye extends proto.Eye with Indices
  lazy val Eye = new Eye

  class ><[
      TAIL <: Indices,
      HEAD <: UpperBound
  ](
      override val tail: TAIL,
      override val head: HEAD
  ) extends proto.><[TAIL, HEAD](tail, head)
      with Impl {}

  implicit def consAlways[TAIL <: Impl, HEAD <: UpperBound] = {

    Cons.from[TAIL, HEAD].to { (tail, head) =>
      new ><(tail, head)
    }
  }
}
