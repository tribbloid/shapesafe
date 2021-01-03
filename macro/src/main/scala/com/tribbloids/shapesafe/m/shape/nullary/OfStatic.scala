package com.tribbloids.shapesafe.m.shape.nullary

import com.tribbloids.shapesafe.m.arity.Dim.<<-
import com.tribbloids.shapesafe.m.arity.Expression
import com.tribbloids.shapesafe.m.shape.Shape.|
import com.tribbloids.shapesafe.m.shape.{OfShape, Shape}
import com.tribbloids.shapesafe.m.~~>
import shapeless.labelled.FieldType
import shapeless.{::, HList, HNil, Witness}

class OfStatic[O <: Shape](val out: O) extends OfShape.Out_=[O] {}

object OfStatic {

  implicit def hNil: HNil ~~> OfStatic[Shape.SNil] = { _ =>
    new OfStatic(Shape.SNil)
  }

  implicit def recursive[
      TAIL <: HList,
      PREV <: Shape,
      N <: String,
      V <: Expression
  ](
      implicit
      observeTail: TAIL ~~> OfStatic[PREV],
      name: Witness.Aux[N]
  ): (FieldType[N, V] :: TAIL) ~~> OfStatic[PREV | (V <<- N)] = {

    { v =>
      val tail = observeTail(v.tail)
      val value = v.head: V

      val head = value <<- name
      val result = new Shape.|(tail.out, head)

      new OfStatic(result)
    }
  }

  def observe[H <: HList, O <: Shape](v: H)(implicit proofOfRecord: H ~~> OfStatic[O]): O = {

    val p = proofOfRecord(v)
    p.out
  }
}
