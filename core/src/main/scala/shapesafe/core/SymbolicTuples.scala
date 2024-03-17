package shapesafe.core

import ai.acyclic.prover.commons.tuple.{ProductTuples, Tuples}
import shapesafe.core.debugging.{HasNotation, Notations}
import ai.acyclic.prover.commons.tuple.ProductTuples.EYE

trait SymbolicTuples[VB] extends Tuples {

  final type VBound = VB

  object Backbone extends ProductTuples[VB]

  trait Tuple extends Backbone.Tuple with HasNotation {

//    final type Cons[HH <: VB] = BoneNotation.this.><[this.type, VB]
    type _ConsExpr[PEEK <: HasNotation]
  }

  class Eye extends Backbone.Eye with Tuple {

    final override type _ConsExpr[T <: HasNotation] = T#Notation
    final override type Notation = EYE.type
  }
  override val Eye = new Eye

  class ><[
      TAIL <: Tuple,
      HEAD <: VB
  ](
      tail: TAIL,
      head: HEAD
  ) extends Backbone.><[TAIL, HEAD](tail, head)
      with Tuple {

    type PeekHead <: HasNotation

    final override type _ConsExpr[PEEK <: HasNotation] = Notations.><[this.Notation, PEEK#Notation]
    final override type Notation = TAIL#_ConsExpr[PeekHead]
  }

  final override def cons[TAIL <: Tuple, HEAD <: VBound](tail: TAIL, head: HEAD): TAIL >< HEAD =
    new ><(tail, head)
}
