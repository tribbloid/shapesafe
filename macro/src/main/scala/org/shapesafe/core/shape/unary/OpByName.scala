package org.shapesafe.core.shape.unary

import org.shapesafe.core.axis.Axis.UB_->>
import org.shapesafe.core.axis.RecordUpdater
import org.shapesafe.core.shape.ProveShape._
import org.shapesafe.core.shape.{LeafShape, ProveShape, Shape}
import shapeless.{::, HList}

import scala.language.implicitConversions

trait OpByName {

  val oldNameUpdater: RecordUpdater

  // all names must be distinctive - no duplication allowed
  trait _On[
      S1 <: Shape
  ] extends Shape {

    val s1: S1
  }

  case class On[
      S1 <: Shape
  ](override val s1: S1)
      extends _On[S1]

  object _On {

    implicit def reduce[
        S1 <: Shape,
        P1 <: LeafShape
    ](
        implicit
        lemma: S1 =>> P1,
        toShape: _Indexing.ToShape.Case[P1#Record]
    ): _On[S1] =>> toShape.Out = {

      ProveShape.forAll[_On[S1]].=>> { v =>
        val p1 = lemma.valueOf(v.s1)
        val result = toShape.apply(p1.record)
        result
      }
    }

  }

  object _Indexing extends ReIndexing.Distinct {

    implicit def consOldName[
        TI <: HList,
        TO <: HList,
        HI <: UB_->>
    ](
        implicit
        consTail: TI ==> TO,
        oldName: oldNameUpdater.Case[(TO, HI)]
    ): (HI :: TI) ==> oldName.Out = {

      forAll[HI :: TI].==> { v =>
        val ti = v.tail
        val to = consTail(ti)
        oldName(to, v.head)
      }

    }
  }
  type _Indexing = _Indexing.type
}