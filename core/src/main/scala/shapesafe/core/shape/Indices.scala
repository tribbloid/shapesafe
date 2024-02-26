package shapesafe.core.shape

import ai.acyclic.prover.commons.tuple.Tuples
import shapesafe.core.SymbolicTuples

trait Indices extends IndicesMagnet with Indices.Backbone.Tuple {

  final override type AsIndices = this.type

  final override def asIndices: Indices.this.type = this
}

object Indices extends Tuples {

  type VBound = Index

  object Backbone extends SymbolicTuples[VBound] {}

  type Tuple = Indices

  class Eye extends Backbone.Eye with Indices
  override val Eye = new Eye

  class ><[
      TAIL <: Indices,
      HEAD <: VBound
  ](
      override val tail: TAIL,
      override val head: HEAD
  ) extends Backbone.><[TAIL, HEAD](tail, head)
      with Tuple {

    override type PeekHead = Head
  }

  override def cons[TAIL <: Tuple, HEAD <: VBound](tail: TAIL, head: HEAD): TAIL >< HEAD = new ><(tail, head)
}
