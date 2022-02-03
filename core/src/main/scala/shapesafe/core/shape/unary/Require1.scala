package shapesafe.core.shape.unary

import shapesafe.core.debugging.Reporters
import shapesafe.core.shape.{ProveShape, ShapeType, StaticShape}

trait Require1 {

  import ProveShape._

  type Condition[_ <: StaticShape]

  type _NotationProto[_]

  type _RefuteProto

  case class On[S1 <: ShapeType](
      s1: S1 with ShapeType
  ) extends Conjecture1.On[S1] {

    override type Notation = _NotationProto[S1#Notation]

    override type _RefuteTxt = _RefuteProto
  }

  object On {

    implicit def simplify[
        S1 <: ShapeType,
        P1 <: StaticShape
    ](
        implicit
        lemma: S1 |- P1,
        condition: Reporters.ForShape.NotFound[Condition[P1], On[P1]]
    ): On[S1] |- P1 = {

      ProveShape.forAll[On[S1]].=>> { v =>
        lemma.instanceFor(v.s1)
      }
    }
  }
}
