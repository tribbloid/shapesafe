package shapesafe.m.viz

import ai.acyclic.prover.commons.testlib.BaseSpec
import ai.acyclic.prover.commons.viz.TypeViz

trait VizCTSpec extends BaseSpec with TypeViz.TestFixtures {

  def VizCT: VizCTSystem

  final lazy val VizRT = VizCT.runtime
}
