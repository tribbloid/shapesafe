package shapesafe.core.shape

import shapesafe.core.debugging.Reporters

object ShapeReporters extends Reporters[ProveShape.type](ProveShape) {

  object PeekShape extends PeekReporter[ShapeType, LeafShape]

  object InterruptShape extends InterruptReporter[ShapeType, LeafShape]
}
