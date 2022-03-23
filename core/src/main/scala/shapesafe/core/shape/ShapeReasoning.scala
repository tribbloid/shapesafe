package shapesafe.core.shape

import shapesafe.core.debugging.Reasoning

object ShapeReasoning extends Reasoning.Canonical {

  val theory: ProveShape.type = ProveShape

  type FromType = ShapeType
  type ToType = LeafShape
}
