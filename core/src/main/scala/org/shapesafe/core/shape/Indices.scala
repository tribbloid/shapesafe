package org.shapesafe.core.shape

import org.shapesafe.core.tuple.{StaticTuples, Tuples}

import scala.language.implicitConversions

trait Indices extends IndicesMagnet with Indices.Proto.Tuple {

  final override type AsIndices = this.type

  final override def asIndices: Indices.this.type = this
}

object Indices extends Tuples {

  type VBound = Index

  object Proto extends StaticTuples[VBound] {}

  type Tuple = Indices

  class Eye extends Proto.Eye with Indices
  override val Eye = new Eye

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

  override def cons[TAIL <: Tuple, HEAD <: VBound](tail: TAIL, head: HEAD): TAIL >< HEAD = new ><(tail, head)
}
