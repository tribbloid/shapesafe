package com.tribbloids.shapesafe.m.util

import shapeless.labelled.FieldType
import shapeless.ops.record.Selector
import shapeless.{HList, Poly1, Witness}

object RecordUtils {

  import shapeless.record._

  case class GetV[H <: HList](hh: H) extends Poly1 {

    implicit def getter[S](
        implicit
        ev: Selector[H, S]
    ): Case.Aux[S, ev.Out] = at[S] { s =>
      val w = Witness(s)
      val _ev = ev.asInstanceOf[Selector[H, w.T]]

      val v = hh.get(w)(_ev)
      v.asInstanceOf[ev.Out]
    }
  }

  case class GetField[H <: HList](hh: H) extends Poly1 {

    implicit def getter[S](
        implicit
        ev: Selector[H, S]
    ): Case.Aux[S, FieldType[S, ev.Out]] = at[S] { s =>
      val w = Witness(s)
      val _ev = ev.asInstanceOf[Selector[H, w.T]]

      val v = hh.fieldAt(w)(_ev)
      v.asInstanceOf[FieldType[S, ev.Out]]
    }
  }
}
