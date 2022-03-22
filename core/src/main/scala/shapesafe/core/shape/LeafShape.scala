package shapesafe.core.shape

import shapeless.Nat
import shapesafe.core.axis.Axis

// TODO: add VerifiedShape
trait LeafShape extends ShapeType {

  type NatNumOfDimensions <: Nat
}

object LeafShape {

  import shapesafe.core.shape.ProveShape._

  implicit def endo[T <: LeafShape]: T |- T = ProveShape.forAll[T].=>>(identity[T])

  import StaticShape.><

  type Vector[T <: Axis] = Shape.^[StaticShape.Eye >< T]
  type Vector_ = Vector[_]

  type Matrix[T1 <: Axis, T2 <: Axis] = Shape.^[StaticShape.Eye >< T1 >< T2]
  type Matrix_ = Matrix[_, _]

  type Tensor3[T1 <: Axis, T2 <: Axis, T3 <: Axis] = Shape.^[StaticShape.Eye >< T1 >< T2 >< T3]
  type Tensor3_ = Tensor3[_, _, _]

  // TODO: use CodeGen to continue this pattern
}
