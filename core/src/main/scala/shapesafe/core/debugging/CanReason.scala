package shapesafe.core.debugging

import ai.acyclic.prover.commons.graph.viz.Hierarchy

trait CanReason {

  val theory: ProofWithReasoning

  protected def expressionType: ExpressionType

  lazy val graph = ExpressionType.asGraph(expressionType)

  def showHierarchy: Hierarchy.Indent2Minimal.Viz[Any] = graph.diagram_hierarchy(Hierarchy.Indent2Minimal)
}

object CanReason {}
