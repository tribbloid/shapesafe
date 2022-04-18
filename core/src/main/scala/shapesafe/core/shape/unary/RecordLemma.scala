package shapesafe.core.shape.unary

import shapesafe.core.AdHocPoly1
import shapesafe.core.axis.Axis.UB_->>
import shapesafe.core.axis.NewNameAppender
import shapesafe.core.shape.StaticShape
import shapeless.{::, HList, HNil}

trait RecordLemma extends AdHocPoly1[HList, HList] {

  implicit val nil: HNil =>> HNil = forAll[HNil].=>> { _ =>
    HNil
  }

  // TODO: move to a more general 'AndThen' class
  object ToShape extends AdHocPoly1[HList, StaticShape] {

    val outer: RecordLemma.this.type = RecordLemma.this

    implicit def toShape[
        I <: HList,
        O <: HList
    ](
        implicit
        lemma1: outer.=>>[I, O],
        lemma2: StaticShape.FromAxes.Case[O]
    ): I =>> lemma2.Out = {

      forAll[I].=>> { i =>
        lemma2.apply(lemma1.apply(i))
      }
    }
  }
}

object RecordLemma {

  trait ConsNewName extends RecordLemma {

    implicit def consNewName[
        TI <: HList,
        TO <: HList,
        HI <: UB_->>
    ](
        implicit
        consTail: TI =>> TO,
        newName: NewNameAppender.Case[(TO, HI)]
    ): (HI :: TI) =>> newName.Out = {
      forAll[HI :: TI].=>> { v =>
        val ti = v.tail
        val to = consTail(ti)
        newName(to -> v.head)
      }
    }

//    implicit def consEmptyName[
//        TI <: HList,
//        TO <: HList,
//        HI <: UB_->>
//    ](
//        implicit
//        consTail: TI =>> TO,
//        newName: NewNameAppender.Case[(TO, HI)]
//    ): (HI :: TI) =>> newName.Out = {
//      forAll[HI :: TI].=>> { v =>
//        val ti = v.tail
//        val to = consTail(ti)
//        newName(to -> v.head)
//      }
//    }
  }
}
