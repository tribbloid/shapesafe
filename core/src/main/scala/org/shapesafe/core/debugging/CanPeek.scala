package org.shapesafe.core.debugging

import com.tribbloids.graph.commons.util.reflect.format.Formats.KindName

trait CanPeek {

  type _OpStr // use singleton-ops
  type _Expr // use TypeVizCT macro

}
