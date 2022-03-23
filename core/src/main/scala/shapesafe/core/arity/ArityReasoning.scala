package shapesafe.core.arity

import shapesafe.core.debugging.Reasoning

object ArityReasoning extends Reasoning.Canonical {

  val theory: ProveArity.type = ProveArity

  type FromType = ArityType
  type ToType = LeafArity
}
