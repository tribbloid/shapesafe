package org.shapesafe.m.viz

import org.shapesafe.graph.commons.testlib.BaseSpec
import org.shapesafe.graph.commons.util.viz.TypeViz

trait VizCTSpec extends BaseSpec with TypeViz.Fixtures {

  def VizCT: VizCTSystem

  final lazy val VizRT = VizCT.runtime
}
