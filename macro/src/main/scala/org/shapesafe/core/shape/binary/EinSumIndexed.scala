package org.shapesafe.core.shape.binary

import org.shapesafe.core.arity.Arity
import org.shapesafe.core.axis.Axis.{->>, UB_->>}
import org.shapesafe.core.tuple.{CanInfix_><, StaticTuples}

import scala.language.implicitConversions

object EinSumIndexed extends StaticTuples[UB_->>] with CanInfix_>< {

  implicit def consIfCondition[
      TAIL <: EinSumIndexed.Impl,
      N <: String,
      D <: Arity
  ](
      implicit
      condition: EinSumAppender.Case[(TAIL#Static, N ->> D)]
  ): Cons.FromFn2[TAIL, N ->> D, TAIL >< (N ->> D)] = {

    Cons.from[TAIL, N ->> D].to { (tail, head) =>
      new ><(tail, head)
    }
  }
}
