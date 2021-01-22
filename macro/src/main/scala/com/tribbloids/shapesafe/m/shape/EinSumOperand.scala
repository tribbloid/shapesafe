package com.tribbloids.shapesafe.m.shape

import com.tribbloids.shapesafe.m.arity.Expression
import com.tribbloids.shapesafe.m.axis.Axis.->>
import com.tribbloids.shapesafe.m.tuple.{CanInfix, StaticTuples, TupleSystem}
import shapeless.{::, HList, HNil}

import scala.language.implicitConversions

trait EinSumOperand[S <: HList] extends EinSumOperand.Backbone.Impl {

  override type Static = S
}

object EinSumOperand extends TupleSystem with CanInfix {

  import EinSumCondition._

  object Backbone extends StaticTuples[(_ <: String) ->> Expression]

  override type UpperBound = Backbone.UpperBound

  override type Impl = Backbone.Impl

  object Eye extends EinSumOperand[HNil] with Backbone.EyeLike

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
      condition: (H_TAIL, (N ->> D)) ==> _
//      obv: Int =:= Int
  ) = {
    new (EinSumOperand[H_TAIL] Cons (N ->> D)) {

      override type Out = ><[H_TAIL, (N ->> D)]

      override def apply(tail: EinSumOperand[H_TAIL], head: (N ->> D)): Out =
        new ><(tail, head)
    }
  }
}
