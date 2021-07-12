package org.shapesafe.core.axis

import org.shapesafe.core.Poly1Base
import shapeless.HList

trait RecordUpdater extends Poly1Base[(HList, Axis.UB_->>), HList] {}
