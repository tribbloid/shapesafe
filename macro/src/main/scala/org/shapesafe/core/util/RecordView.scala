package org.shapesafe.core.util

import org.shapesafe.core.shape.Index
import shapeless.labelled.FieldType
import shapeless.ops.hlist.At
import shapeless.ops.record.Selector
import shapeless.{HList, Nat, Poly1}

case class RecordView[H <: HList](hh: H) {

  //https://stackoverflow.com/questions/66036106/can-shapeless-record-type-be-used-as-a-poly1-part-2
  trait GetV extends Poly1 {

    implicit def getter[S](
        implicit
        _selector: Selector[H, S]
    ): Case.Aux[S, _selector.Out] = at[S] { _ =>
      _selector(hh)
    }
  }
  object GetV extends GetV

  trait GetField extends Poly1 {

    implicit def getter[S](
        implicit
        _selector: Selector[H, S]
    ): Case.Aux[S, FieldType[S, _selector.Out]] = at[S] { _ =>
      _selector(hh).asInstanceOf[FieldType[S, _selector.Out]]
    }
  }
  object GetField extends GetField

}

object RecordView {}
