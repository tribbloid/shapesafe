package org.shapesafe.core.shape

package object unary {

  object EinSumIndexed extends EinSumIndexedLike
  type EinSumIndexed = EinSumIndexed.Impl

  object DistinctIndexed extends DistinctIndexedLike
  type DistinctIndexed = DistinctIndexed.Impl
}
