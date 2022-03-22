package shapesafe.core.shape.unary

import shapesafe.core.debugging.Refutes
import shapesafe.core.shape.ProveShape.|-
import shapesafe.core.shape.{LeafShape, ProveShape, ShapeType}

trait Require1 extends Conjecture1 {

  type Prior <: LeafShape
  val s1: SS1 with ShapeType

  type Condition[_ <: Prior]

//  type Constructor[N1 <: ShapeType] <: Require1
}

object Require1 {

  implicit def simplify[
      T <: Require1,
      P1 <: T#Prior
  ](
      implicit
      lemma: T#SS1 |- P1,
      condition: Refutes.ForShape.NotFoundInfo[T#Condition[P1], T]
  ): T |- P1 = {

    ProveShape.forAll[T].=>> { v =>
      lemma.instanceFor(v.s1)
    }
  }
}
