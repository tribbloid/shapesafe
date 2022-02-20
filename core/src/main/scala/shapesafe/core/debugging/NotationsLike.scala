package shapesafe.core.debugging

trait NotationsLike {

  import NotationsLike._

//  type Peek[T <: CanPeek] = T#_Peek // with T

  trait :<<-[A, B]
  trait ><[A, B]

  //  trait Infix[A, S, B] extends (A ~~ Only[S] ~~ B)
  //  trait PrefixW1[S, A] extends (Only[S] ~~ A)
  //  trait PrefixW2[S, A, B] extends (Only[S] ~~ A ~~ B)

  trait :<<=[A, B]
//  trait OuterProduct[A, B] use >< instead

  trait RequireDistinctNames[A]

  trait RequireNumOfDimensions[A, NAT]

  trait Select1[A, B]
  trait Rearrange[A, B]

  trait AppendByName[O] extends Proto1 {
//    type _NotationTxt = "AppendByName[" + O + "]"
  }
  trait ReduceByName[O] extends Proto1 {
//    type _NotationTxt = "ReduceByName[" + O + "]"
  }
  trait Op2ByDim_Strict[O] extends Proto2 {
//    type _NotationTxt = "Op2ByDim_Strict[" + O + "]"
  }
  trait Op2ByDim_DropLeft[O] extends Proto2 {
//    type _NotationTxt = "Op2ByDim_DropRight[" + O + "]"
  }
}

object NotationsLike {

  trait Proto1 {

    trait On[A] {}

    type Apply[AA <: CanPeek] = On[AA#Notation]
  }
  trait Proto2 {

    trait On[A, B] {}

    type Apply[AA <: CanPeek, BB <: CanPeek] = On[AA#Notation, BB#Notation]
  }

}
