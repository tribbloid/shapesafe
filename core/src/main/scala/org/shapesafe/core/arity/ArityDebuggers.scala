package org.shapesafe.core.arity

import org.shapesafe.core.debugging.Debuggers

object ArityDebuggers extends Debuggers[ProveArity.type](ProveArity) {

  object PeekArity extends PeekFn[Arity, LeafArity]
}
