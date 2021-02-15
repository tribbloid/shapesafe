package org.shapesafe.core.util

import shapeless.labelled.FieldType
import shapeless.ops.hlist.At
import shapeless.ops.record.Selector
import shapeless.{HList, Nat, Poly1}

case class RecordView[H <: HList](hh: H) {

  //https://stackoverflow.com/questions/66036106/can-shapeless-record-type-be-used-as-a-poly1-part-2
  trait GetV extends Poly1 {

    implicit def getter[S](
        implicit
        ev: Selector[H, S]
    ): Case.Aux[S, ev.Out] = at[S] { _ =>
      ev(hh)
    }
  }
  object GetV extends GetV

  trait GetField extends Poly1 {

    implicit def getter[S](
        implicit
        ev: Selector[H, S]
    ): Case.Aux[S, FieldType[S, ev.Out]] = at[S] { _ =>
      ev(hh).asInstanceOf[FieldType[S, ev.Out]]
    }
  }
  object GetField extends GetField

  trait FindField extends Poly1 {

    implicit def byKey[S, BY <: Finder.ByName[S]](
        implicit
        ev: Selector[H, S]
    ): Case.Aux[BY, ev.Out] = at[BY] { _ =>
      ev(hh).asInstanceOf[FieldType[S, ev.Out]]
    }

    implicit def byOrdinal[N <: Nat, BY <: Finder.ByOrdinal[N]](
        implicit
        ev: At[H, N]
    ): Case.Aux[BY, ev.Out] = at[BY] { _ =>
      ev(hh)
    }
  }
}

object RecordView {}
