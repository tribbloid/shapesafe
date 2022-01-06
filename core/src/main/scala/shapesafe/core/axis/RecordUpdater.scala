package shapesafe.core.axis

import shapesafe.core.Poly1Base
import shapeless.HList

trait RecordUpdater extends Poly1Base[(HList, Axis.UB_->>), HList] {}
