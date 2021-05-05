package org.shapesafe.core.shape

import org.shapesafe.core.debugging.OpsUtil.Peek
import org.shapesafe.core.tuple.{CanInfix_><, StaticTuples, TupleSystem}

import scala.language.implicitConversions

trait Indices extends IndicesMagnet with Indices.proto.Impl {

  final override type AsIndices = this.type

  final override def asIndices: Indices.this.type = this
}

object Indices extends TupleSystem with CanInfix_>< {

  type UpperBound = Index

  object proto extends StaticTuples.Total[UpperBound] with CanInfix_>< {}

  type Impl = Indices

  class Eye extends proto.Eye with Indices
  lazy val Eye = new Eye

  class ><[
      TAIL <: Indices,
      HEAD <: UpperBound
  ](
      override val tail: TAIL,
      override val head: HEAD
  ) extends proto.><[TAIL, HEAD](tail, head)
      with Impl {

    override type _PeekHead = Peek[Head]
  }

  implicit def consAlways[TAIL <: Impl, HEAD <: UpperBound] = {

    Cons.from[TAIL, HEAD].to { (tail, head) =>
      new ><(tail, head)
    }
  }

}
