package shapesafe.core.arity

import shapesafe.core.debugging.Reasoning

object ArityReasoning extends Reasoning[ProveArity.type](ProveArity) {

  object PeekArity extends PeekReporter[ArityType, LeafArity]

  object InterruptArity extends InterruptReporter[ArityType, LeafArity]
}
