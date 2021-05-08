package org.shapesafe.core.shape.unary

import com.tribbloids.graph.commons.util.HasOuter
import org.shapesafe.core.axis.Axis.UB_->>
import org.shapesafe.core.axis.RecordUpdater
import org.shapesafe.core.debugging.OpsUtil.Peek
import org.shapesafe.core.debugging.symbol
import org.shapesafe.core.shape.{LeafShape, ProveShape, Shape}
import shapeless.{::, HList}

import scala.language.implicitConversions

trait ReduceByName {

  import ProveShape.Factory._
  import ProveShape._

  type _Unary <: symbol.Unary

  val oldNameUpdater: RecordUpdater

  // all names must be distinctive - no duplication allowed
  trait _On[
      S1 <: Shape
  ] extends Conjecture1.^[S1]
      with HasOuter {

    override def outer: ReduceByName.this.type = ReduceByName.this

    def s1: S1 with Shape

    override type _Ops = Peek.PrefixW1[_Unary#Lit, S1]
    override type _Ovrd = _Unary#On[S1#Ovrd]
  }

  object _On {

    implicit def simplify[
        S1 <: Shape,
        P1 <: LeafShape
    ](
        implicit
        lemma: S1 |- P1,
        toShape: _Indexing.ToShape.Case[P1#Record]
    ): _On[S1] =>> toShape.Out = {

      ProveShape.forAll[_On[S1]].=>> { v =>
        val p1 = lemma.valueOf(v.s1)
        val result = toShape.apply(p1.record)
        result
      }
    }
  }

  case class On[
      S1 <: Shape
  ](
      override val s1: S1 with Shape
  ) extends _On[S1] {}

  object _Indexing extends UnaryIndexingFn.Distinct {

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
