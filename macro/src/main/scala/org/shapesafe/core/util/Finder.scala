package org.shapesafe.core.util

import com.tribbloids.graph.commons.util.IDMixin
import shapeless.{Nat, Witness}

import scala.language.implicitConversions

trait Finder extends IDMixin {

  override lazy val toString: String = s"${_id}:${getClass.getSimpleName}"
}

object Finder {

  trait HasKey[+KUB] extends Finder {}
  type Str = HasKey[String]

  class ByKey[S](key: S) extends HasKey[S] {
    override protected def _id: Any = key

  }

  object ByKey {

    def apply(w: Witness): ByKey[w.T] = new ByKey[w.T](w.value)
  }

  class ByOrdinal[N <: Nat](n: N) extends HasKey[Nothing] {
    override protected def _id: Any = n
  }

  // also a magnet of Nat & String Literals

  implicit def fromNat[N <: Nat](n: N): ByOrdinal[N] = new ByOrdinal(n)

  implicit def fromW(w: Witness): ByKey[w.T] = ByKey(w)

}
