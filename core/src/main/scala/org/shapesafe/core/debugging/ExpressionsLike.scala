package org.shapesafe.core.debugging

import org.shapesafe.core.debugging.DebugSymbol.{On1, On2}

trait ExpressionsLike {

  type Expr[T <: CanPeek] = T#_AsExpr // with T

  trait :<<-[A, B]
  trait ><[A, B]

  //  trait Infix[A, S, B] extends (A ~~ Only[S] ~~ B)
  //  trait PrefixW1[S, A] extends (Only[S] ~~ A)
  //  trait PrefixW2[S, A, B] extends (Only[S] ~~ A ~~ B)

  trait |<<-[A, B]
//  trait OuterProduct[A, B] use >< instead
  trait RequireDistinct[A]
  trait GetSubscript[A, B]
  trait Reorder[A, B]

  import singleton.ops.+

  trait AppendByName[O] extends On1 {
    type _AsOpStr = "AppendByName[" + O + "]"
  }
  trait SquashByName[O] extends On1 {
    type _AsOpStr = "SquashByName[" + O + "]"
  }
  trait DimensionWise[O] extends On2 {
    type _AsOpStr = "DimensionWise[" + O + "]"
  }
}
