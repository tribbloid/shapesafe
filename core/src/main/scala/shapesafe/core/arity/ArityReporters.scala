package shapesafe.core.arity

import shapesafe.core.debugging.Reporters

object ArityReporters extends Reporters[ProveArity.type](ProveArity) {

  object PeekArity extends PeekReporter[Arity, LeafArity]

  object InterruptArity extends InterruptReporter[Arity, LeafArity]
}
