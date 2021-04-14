package org.shapesafe.core.shape

import org.shapesafe.core.debugging.Debuggers

object ShapeDebuggers extends Debuggers[ProveShape.type](ProveShape) {

  object PeekShape extends PeekFn[Shape, LeafShape]
}
