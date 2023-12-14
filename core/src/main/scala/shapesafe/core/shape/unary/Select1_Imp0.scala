package shapesafe.core.shape.unary

import shapesafe.core.debugging.Refutes
import shapesafe.core.shape._
import shapesafe.m.Emit

trait Select1_Imp0 {

  import ProveShape._

  implicit def refute[
      S1 <: ShapeType,
      P1 <: LeafShape,
      I <: Index,
      MSG
  ](
      implicit
      lemma1: S1 |- P1,
      refute0: Refutes.ForShape.Refute0[Select1[P1, I], MSG],
      msg: Emit.Error[MSG]
  ): Select1[S1, I] |- LeafShape = {
    ???
  }
}
