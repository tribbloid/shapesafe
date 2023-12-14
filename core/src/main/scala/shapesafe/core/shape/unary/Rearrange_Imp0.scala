package shapesafe.core.shape.unary

import shapesafe.core.debugging.Refutes
import shapesafe.core.shape._
import shapesafe.m.Emit

trait Rearrange_Imp0 {

  import ProveShape._

  implicit def refute[
      S1 <: ShapeType,
      P1 <: LeafShape,
      II <: IndicesMagnet,
      MSG
  ](
      implicit
      lemma: S1 |- P1,
      refute0: Refutes.ForShape.Refute0[Rearrange[P1, II], MSG],
      msg: Emit.Error[MSG]
  ): Rearrange[S1, II] |- LeafShape = {
    ???
  }

}
