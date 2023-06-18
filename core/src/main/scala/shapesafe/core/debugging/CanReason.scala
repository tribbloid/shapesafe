package shapesafe.core.debugging

import ai.acyclic.prover.commons.graph.viz.{Flow, Hierarchy}

trait CanReason {

  val theory: ProofWithReasoning

  protected def expressionType: ExpressionType

  lazy val graph = ExpressionType.asGraph(expressionType)

  lazy val diagram_hierarchy: Hierarchy.Indent2Minimal.Viz[Any] = graph.diagram_hierarchy(Hierarchy.Indent2Minimal)

  lazy val diagram_flow: Flow.Default.Viz[Any] = graph.diagram_flow(Flow.Default)
}

object CanReason {}
