package org.shapesafe.core.debugging

import com.tribbloids.graph.commons.util.reflect.format.Formats.KindName

trait CanPeek {

  type _Ops // use singleton-ops
  type _Ovrd // use TypeVizCT macro

  final type Ovrd = _Ovrd with KindName[this.type]
}
