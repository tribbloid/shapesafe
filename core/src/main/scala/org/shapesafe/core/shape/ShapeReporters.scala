package org.shapesafe.core.shape

import org.shapesafe.core.debugging.Reporters

object ShapeReporters extends Reporters[ProveShape.type](ProveShape) {

  object PeekShape extends PeekReporter[Shape, LeafShape]

  object InterruptShape extends ErrorReporter[Shape, LeafShape]
}
