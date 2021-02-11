package org.shapesafe.core.shape.op

import org.shapesafe.core.arity.Arity
import org.shapesafe.core.axis.Axis.->>
import org.shapesafe.core.tuple.{CanFromStatic, CanInfix_><, StaticTuples, TupleSystem}
import shapeless.{::, HList, HNil}

import scala.language.implicitConversions

trait EinSumIndexed[S <: HList] extends EinSumIndexed.Proto {

  override type Static = S
}

object EinSumIndexed extends TupleSystem with CanInfix_>< with CanFromStatic {

  object Proto extends StaticTuples[(_ <: String) ->> Arity]
  type Proto = Proto.Impl

  override type UpperBound = Proto.UpperBound

  override type Impl = Proto.Impl

  object eye extends EinSumIndexed[HNil] with Proto.EyeLike

  class ><[
      H_TAIL <: HList,
      HEAD <: UpperBound
  ](
      tail: EinSumIndexed[H_TAIL],
      head: HEAD
  ) extends Proto.><[EinSumIndexed[H_TAIL], HEAD](
        tail,
        head
      )
      with EinSumIndexed[HEAD :: H_TAIL]

  implicit def consIfNoConflict[
      H_TAIL <: HList,
      N <: String,
      D <: Arity
  ](
      implicit
      condition: EinSumCondition.Case[(H_TAIL, N ->> D)]
  ): Cons.FromFn[EinSumIndexed[H_TAIL], N ->> D, H_TAIL >< (N ->> D)] = {

    Cons[EinSumIndexed[H_TAIL], N ->> D].to { (tail, head) =>
      new ><(tail, head)
    }
  }
}
