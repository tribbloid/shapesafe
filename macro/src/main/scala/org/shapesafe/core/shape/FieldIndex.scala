package org.shapesafe.core.shape

import com.tribbloids.graph.commons.util.IDMixin
import shapeless.ops.nat.ToInt
import shapeless.{Nat, Witness}

import scala.language.implicitConversions

trait FieldIndex extends IDMixin {

  override lazy val toString: String = s"${_id}:${getClass.getSimpleName}"
}

object FieldIndex {

  trait Key_<:[+KUB] extends FieldIndex {}
  type Str = Key_<:[String]

  class Named[S <: String](val w: Witness.Aux[S]) extends Key_<:[S] {
    def name: S = w.value
    type Name = S

    override protected def _id = w.value

  }

  object Named {

    def apply[S <: String](w: Witness.Aux[S]): Named[S] = new Named(w)
  }

  class N_th[N <: Nat](val index: N, indexInt: Int) extends Key_<:[Nothing] {
    type Ordinal = N

    override protected def _id = indexInt
  }

  object N_th {

    def apply(i: Nat)(
        implicit
        toIntN: ToInt[i.N]
    ) = new N_th[i.N](i.asInstanceOf[i.N], toIntN.apply())
  }
}
