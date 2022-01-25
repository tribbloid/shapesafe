package shapesafe.core

import shapesafe.core.arity.ops.ArityOpsLike
import shapesafe.core.shape.Shape
import shapesafe.core.shape.binary.OuterProduct
import shapesafe.core.shape.ops.EinSumOp

object Ops extends ArityOpsLike {

  type _ArityType = Nothing
  override def arityType: _ArityType = ???

  object EinSum {

    def apply(s1: Shape): EinSumOp[s1._ShapeType] = {
      EinSumOp[s1._ShapeType](s1.shapeType)
    }

    def apply(s1: Shape, s2: Shape): EinSumOp[OuterProduct[s1._ShapeType, s2._ShapeType]] = {

      EinSumOp((s1 >< s2).shapeType)
    }
  }
}
