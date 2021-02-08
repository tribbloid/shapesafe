package org.shapesafe.core.util

import shapeless.labelled.FieldType
import shapeless.ops.record.Selector
import shapeless.{HList, Poly1, Witness}

object RecordUtils {

  import shapeless.record._

  //https://stackoverflow.com/questions/66036106/can-shapeless-record-type-be-used-as-a-poly1-part-2
  case class GetV[H <: HList](hh: H) extends Poly1 {

    implicit def getter[S](
        implicit
        ev: Selector[H, S]
    ): Case.Aux[S, ev.Out] = at[S] { _ =>
      ev(hh)
    }
  }

  case class GetField[H <: HList](hh: H) extends Poly1 {

    implicit def getter[S](
        implicit
        ev: Selector[H, S]
    ): Case.Aux[S, FieldType[S, ev.Out]] = at[S] { _ =>
      ev(hh).asInstanceOf[FieldType[S, ev.Out]]
    }
  }
}
