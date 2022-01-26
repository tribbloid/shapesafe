package shapesafe.core.debugging

import shapesafe.core.debugging.HasDebugSymbol.{ExprOn1, ExprOn2}

trait ExpressionsLike {

//  type Peek[T <: CanPeek] = T#_Peek // with T

  trait :<<-[A, B]
  trait ><[A, B]

  //  trait Infix[A, S, B] extends (A ~~ Only[S] ~~ B)
  //  trait PrefixW1[S, A] extends (Only[S] ~~ A)
  //  trait PrefixW2[S, A, B] extends (Only[S] ~~ A ~~ B)

  trait :<<=[A, B]
//  trait OuterProduct[A, B] use >< instead
  trait RequireDistinct[A]
  trait GetSubscript[A, B]
  trait Reorder[A, B]

  import singleton.ops.+

  trait AppendByName[O] extends ExprOn1 {
    type _DebugSymbol = "AppendByName[" + O + "]"
  }
  trait ReduceByName[O] extends ExprOn1 {
    type _DebugSymbol = "ReduceByName[" + O + "]"
  }
  trait Op2PerDim[O] extends ExprOn2 {
    type _DebugSymbol = "Op2PerDim[" + O + "]"
  }
}
