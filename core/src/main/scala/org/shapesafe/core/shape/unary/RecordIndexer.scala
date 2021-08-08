package org.shapesafe.core.shape.unary

import org.shapesafe.core.Poly1Base
import org.shapesafe.core.axis.Axis.UB_->>
import org.shapesafe.core.axis.NewNameAppender
import org.shapesafe.core.shape.StaticShape
import shapeless.{::, HList, HNil}

trait RecordIndexer extends Poly1Base[HList, HList] {

  implicit val nil: HNil ==> HNil = forAll[HNil].==> { _ =>
    HNil
  }

  // TODO: move to a more general 'AndThen' class
  object ToShape extends Poly1Base[HList, StaticShape] {

    val outer: RecordIndexer.this.type = RecordIndexer.this

    implicit def toShape[
        I <: HList,
        O <: HList
    ](
        implicit
        lemma1: outer.==>[I, O],
        lemma2: StaticShape.FromRecord.Case[O]
    ): I ==> lemma2.Out = {

      forAll[I].==> { i =>
        lemma2.apply(lemma1.apply(i))
      }
    }
  }
}

object RecordIndexer {

  trait DistinctLike extends RecordIndexer {

    implicit def consNewName[
        TI <: HList,
        TO <: HList,
        HI <: UB_->>
    ](
        implicit
        consTail: TI ==> TO,
        newName: NewNameAppender.Case[(TO, HI)]
    ): (HI :: TI) ==> newName.Out = {
      forAll[HI :: TI].==> { v =>
        val ti = v.tail
        val to = consTail(ti)
        newName(to -> v.head)
      }
    }
  }
}
