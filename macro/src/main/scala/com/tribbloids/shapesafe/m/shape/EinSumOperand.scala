package com.tribbloids.shapesafe.m.shape

import com.tribbloids.shapesafe.m.arity.Expression
import com.tribbloids.shapesafe.m.axis.Axis.->>
import com.tribbloids.shapesafe.m.tuple.{CanFromStatic, CanInfix_><, StaticTuples, TupleSystem}
import shapeless.{::, HList, HNil}

import scala.language.implicitConversions

trait EinSumOperand[S <: HList] extends EinSumOperand.Backbone.Impl {

  override type Static = S
}

object EinSumOperand extends TupleSystem with CanInfix_>< with CanFromStatic {

  object Backbone extends StaticTuples[(_ <: String) ->> Expression]

  override type UpperBound = Backbone.UpperBound

  override type Impl = Backbone.Impl

  object eye extends EinSumOperand[HNil] with Backbone.EyeLike

  class ><[
      H_TAIL <: HList,
      HEAD <: UpperBound
  ](
      tail: EinSumOperand[H_TAIL],
      head: HEAD
  ) extends Backbone.><[EinSumOperand[H_TAIL], HEAD](
        tail,
        head
      )
      with EinSumOperand[HEAD :: H_TAIL]

  implicit def consIfNoConflict[
      H_TAIL <: HList,
      N <: String,
      D <: Expression
  ](
      implicit
      condition: EinSumCondition.Case[(H_TAIL, N ->> D)]
//      obv: Int =:= Int
  ): Cons.FromFn[EinSumOperand[H_TAIL], N ->> D, H_TAIL >< (N ->> D)] = {

    Cons[EinSumOperand[H_TAIL], N ->> D].build { (tail, head) =>
      new ><(tail, head)
    }
  }
}
