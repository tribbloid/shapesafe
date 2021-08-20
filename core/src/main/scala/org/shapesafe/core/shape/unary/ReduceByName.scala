package org.shapesafe.core.shape.unary

import org.shapesafe.core.axis.Axis.UB_->>
import org.shapesafe.core.axis.RecordUpdater
import org.shapesafe.core.debugging.Expressions.Expr
import org.shapesafe.core.debugging.{DebugSymbol, OpStrs}
import org.shapesafe.core.shape.{ProveShape, Shape, StaticShape}
import org.shapesafe.graph.commons.util.HasOuter
import shapeless.{::, HList}

import scala.language.implicitConversions

trait ReduceByName {

  import ProveShape.ForAll._
  import ProveShape._

  type _Unary <: DebugSymbol.On1

  val oldNameUpdater: RecordUpdater

  // all names must be distinctive - no duplication allowed
  trait _On[
      S1 <: Shape
  ] extends Conjecture1.^[S1]
      with HasOuter {

    override def outer: ReduceByName.this.type = ReduceByName.this

    def s1: S1 with Shape

    override type _AsOpStr = OpStrs.PrefixW1[_Unary#_AsOpStr, S1]
    override type _AsExpr = _Unary#On[Expr[S1]]
  }

  object _On {

    implicit def simplify[
        S1 <: Shape,
        P1 <: StaticShape
    ](
        implicit
        lemma: S1 |- P1,
        toShape: _Lemma.ToShape.Case[P1#Record]
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

  object _Lemma extends RecordLemma.ConsNewName {

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

  type _Indexing = _Lemma.type
}
