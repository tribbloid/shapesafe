package shapesafe.core.arity

import shapesafe.core.debugging.Reporters

object ArityReporters extends Reporters[ProveArity.type](ProveArity) {

  object PeekArity extends PeekReporter[ArityType, LeafArity]

  object InterruptArity extends InterruptReporter[ArityType, LeafArity]
}
