package shapesafe.core.arity.binary

import shapesafe.core.arity.ProveArity.{|-, |-<}
import shapesafe.core.arity.{ArityType, LeafArity}
import shapesafe.core.debugging.Refutes.ForArity
import shapesafe.m.Emit

trait Op2Like_Imp0 {

  implicit def refute[
      A1 <: ArityType,
      A2 <: ArityType,
      P1 <: LeafArity,
      P2 <: LeafArity,
      OP <: Op2Like,
      MSG
  ](
      implicit
      bound1: A1 |-< P1,
      bound2: A2 |-< P2,
      refute0: ForArity.Refute0[OP#On[P1, P2], MSG],
      emit: Emit.Error[MSG]
  ): OP#On[A1, A2] |- LeafArity = {
    ???
  }
}
