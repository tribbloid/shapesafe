package shapesafe.core.shape.unary

import shapesafe.core.debugging.Refutes
import shapesafe.core.shape.{LeafShape, Names, ShapeType}
import shapesafe.m.Emit

trait GiveNames_Imp0 {

  import shapesafe.core.shape.ProveShape._

  implicit def refute[
      S1 <: ShapeType,
      P1 <: LeafShape,
      N <: Names,
      MSG
  ](
      implicit
      lemma: S1 |- P1,
      refute0: Refutes.ForShape.Refute0[GiveNames[P1, N], MSG],
      msg: Emit.Error[MSG]
  ): GiveNames[S1, N] |- LeafShape = {
    ???
  }
}
