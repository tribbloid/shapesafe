package org.shapesafe.core.debugging

import com.tribbloids.graph.commons.util.reflect.format.Formats
import com.tribbloids.graph.commons.util.viz.TypeViz
import org.shapesafe.BaseSpec

class DebuggingUtilSpec extends BaseSpec {

  import DebuggingUtilSpec._

  val viz = TypeViz.formattedBy(Formats.TypeImpl.Both)

  val jj = new JJ {}

  it("inner type") {

    viz[jj.jj2.LL].should_=:=()
  }
}

object DebuggingUtilSpec {

  trait JJ {

    lazy val jj2 = new JJ {}

    type LL
  }

  type KK = JJ
}
