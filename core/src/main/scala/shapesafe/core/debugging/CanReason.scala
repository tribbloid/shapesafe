package shapesafe.core.debugging

import ai.acyclic.prover.commons.graph.viz.Hierarchy

trait CanReason {

  val theory: ProofWithReasoning

  protected def expressionType: ExpressionType

  lazy val graph = ExpressionType.AsGraph(expressionType)

  def showHierarchy: Hierarchy.Indent2Minimal.Viz[Any] = graph.showHierarchy(Hierarchy.Indent2Minimal)
}

object CanReason {}
