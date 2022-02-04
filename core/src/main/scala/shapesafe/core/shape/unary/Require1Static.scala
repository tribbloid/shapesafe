package shapesafe.core.shape.unary

import shapesafe.core.debugging.Reporters
import shapesafe.core.shape.ProveShape.|-
import shapesafe.core.shape.{ProveShape, ShapeType, StaticShape}

trait Require1Static extends Conjecture1 {

  val s1: SS1 with ShapeType

  type Condition[P1 <: StaticShape]

  type Constructor[N1 <: ShapeType] <: Require1Static
}

object Require1Static {

  implicit def simplify[
      T <: Require1Static,
      P1 <: StaticShape
  ](
      implicit
      lemma: T#SS1 |- P1,
      condition: Reporters.ForShape.NotFoundInfo[T#Condition[P1], T]
  ): T |- P1 = {

    ProveShape.forAll[T].=>> { v =>
      lemma.instanceFor(v.s1)
    }
  }
}
