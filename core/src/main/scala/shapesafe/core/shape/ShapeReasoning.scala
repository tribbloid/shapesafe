package shapesafe.core.shape

import shapesafe.core.debugging.Reasoning

object ShapeReasoning extends Reasoning[ProveShape.type](ProveShape) {

  object PeekShape extends PeekReporter[ShapeType, LeafShape]

  object InterruptShape extends InterruptReporter[ShapeType, LeafShape]
}
