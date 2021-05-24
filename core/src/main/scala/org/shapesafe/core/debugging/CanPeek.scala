package org.shapesafe.core.debugging

trait CanPeek {

  type _AsStr // use singleton-ops
  type _AsExpr // use TypeVizCT macro

}
