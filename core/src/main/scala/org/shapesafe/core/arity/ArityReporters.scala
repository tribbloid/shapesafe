package org.shapesafe.core.arity

import org.shapesafe.core.debugging.Reporters

object ArityReporters extends Reporters[ProveArity.type](ProveArity) {

  object PeekArity extends PeekReporter[Arity, LeafArity]
}
