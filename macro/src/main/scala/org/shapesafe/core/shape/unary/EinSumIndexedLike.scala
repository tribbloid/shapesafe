package org.shapesafe.core.shape.unary

import org.shapesafe.core.arity.Arity
import org.shapesafe.core.axis.Axis.->>
import org.shapesafe.core.axis.ExistingKVAppender

import scala.language.implicitConversions

trait EinSumIndexedLike extends DistinctIndexedLike {

  implicit def consIfOldName[
      TAIL <: Impl,
      N <: String,
      D <: Arity
  ](
      implicit
      condition: ExistingKVAppender.Case[(TAIL#Static, N ->> D)]
  ): Cons.FromFn2[TAIL, N ->> D, TAIL >< (N ->> D)] = {

    Cons.from[TAIL, N ->> D].to { (tail, head) =>
      new ><(tail, head)
    }
  }
}
