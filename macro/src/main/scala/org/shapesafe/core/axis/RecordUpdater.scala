package org.shapesafe.core.axis

import org.shapesafe.core.Poly1Base
import org.shapesafe.core.axis.Axis.UB_->>
import shapeless.HList

trait RecordUpdater extends Poly1Base[(HList, UB_->>), HList] {}
