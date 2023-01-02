package shapesafe.core.debugging

import ai.acyclic.prover.commons.graph.local.impl.ProductDiscovery

trait ExpressionType extends CanPeek {}

object ExpressionType {

  trait Leaf extends ExpressionType with ProductDiscovery.Exclude

  case class AsGraph(override val root: ExpressionType) extends ProductDiscovery[ExpressionType] {}
}
