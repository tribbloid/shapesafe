package shapesafe.m.viz

import ai.acyclic.graph.commons.testlib.BaseSpec
import ai.acyclic.graph.commons.viz.TypeViz

trait VizCTSpec extends BaseSpec with TypeViz.Fixtures {

  def VizCT: VizCTSystem

  final lazy val VizRT = VizCT.runtime
}
