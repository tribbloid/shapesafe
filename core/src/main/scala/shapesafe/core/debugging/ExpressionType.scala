package shapesafe.core.debugging

import ai.acyclic.prover.commons.graph.local.Local
import ai.acyclic.prover.commons.meta2.ProductDiscovery

trait ExpressionType extends CanPeek {}

object ExpressionType {

  trait Leaf extends ExpressionType with ProductDiscovery.Exclude

  def asGraph(root: ExpressionType) = {

    Local.AnyGraph(ProductDiscovery.apply(root))
  }

//  case class AsGraph(override val root: ExpressionType) extends ProductDiscovery[ExpressionType] {}
}
