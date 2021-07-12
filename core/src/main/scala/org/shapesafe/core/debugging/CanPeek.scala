package org.shapesafe.core.debugging

trait CanPeek {

  type _AsOpStr // use singleton-ops TODO: remove! Not efficient
  type _AsExpr // use TypeVizCT macro
}
