package shapesafe.core.axis

import shapesafe.core.AdHocPoly1
import shapeless.HList

trait RecordUpdater extends AdHocPoly1[(HList, Axis.UB_->>), HList] {}
