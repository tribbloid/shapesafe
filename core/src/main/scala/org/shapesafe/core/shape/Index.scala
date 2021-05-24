package org.shapesafe.core.shape

import com.tribbloids.graph.commons.util.IDMixin
import com.tribbloids.graph.commons.util.reflect.format.FormatOvrd.Only
import org.shapesafe.core.debugging.CanPeek
import org.shapesafe.core.debugging.DebuggingUtil.StrOrRaw
import shapeless.ops.nat.ToInt
import shapeless.{Nat, Witness}
import singleton.ops.ToString

import scala.language.implicitConversions

trait Index extends IDMixin with CanPeek {

  override lazy val toString: String = s"${_id}:${getClass.getSimpleName}"
}

object Index {

  trait Name_<:[+KUB] extends Index {}
  type Str = Name_<:[String]

  class Name[S <: String](val w: Witness.Aux[S]) extends Name_<:[S] {
    def name: S = w.value
    type Name = S

    override protected def _id = w.value

    override type _OpStr = StrOrRaw[S]
    override type _Expression = S
  }

  object Name {

    def apply(w: Witness.Lt[String]): Name[w.T] = new Name(w)
  }

  class I_th[N <: Nat](val index: N, indexInt: Int) extends Name_<:[Nothing] {
    type Ordinal = N

    override protected def _id = indexInt

    // TODO: type string is too long
    override type _OpStr = StrOrRaw[ToString[N]]
    override type _Expression = ToString[N]
  }

  object I_th {

    def apply(i: Nat)(
        implicit
        toIntN: ToInt[i.N]
    ) = new I_th[i.N](i.asInstanceOf[i.N], toIntN.apply())
  }
}
