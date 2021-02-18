package org.shapesafe.core.shape.binary

import org.shapesafe.core.Poly1Base
import org.shapesafe.core.axis.Axis.UB_->>
import shapeless.HList

trait FieldAppender extends Poly1Base[(HList, UB_->>), HList] {}
