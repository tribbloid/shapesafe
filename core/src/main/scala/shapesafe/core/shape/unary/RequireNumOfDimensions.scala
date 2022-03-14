package shapesafe.core.shape.unary

import shapeless.Nat
import shapesafe.core.debugging.Notations
import shapesafe.core.shape.{ShapeType, StaticShape}
import singleton.ops.{+, ToString}

// TODO: arg should be an Arity
case class RequireNumOfDimensions[
    S1 <: ShapeType,
    N <: Nat
](
    override val s1: S1 with ShapeType,
    n: N
) extends Conjecture1.On[S1]
    with Require1 {

  override type Prior = StaticShape

  override type Condition[P1 <: StaticShape] = P1#NatNumOfDimensions =:= n.N

  override type Notation = Notations.RequireNumOfDimensions[S1#Notation, n.N]

  override type _RefuteTxt = "Accepting only " +
    ToString[N] +
    " dimension(s)"
}
