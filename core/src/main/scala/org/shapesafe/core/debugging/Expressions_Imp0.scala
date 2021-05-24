package org.shapesafe.core.debugging

trait Expressions_Imp0 {

  type Expr[T <: CanPeek] = T#_Expression // with T

  trait :<<-[A, B]
  trait ><[A, B]

  //  trait Infix[A, S, B] extends (A ~~ Only[S] ~~ B)
  //  trait PrefixW1[S, A] extends (Only[S] ~~ A)
  //  trait PrefixW2[S, A, B] extends (Only[S] ~~ A ~~ B)

  trait |<<-[A, B]
//  trait OuterProduct[A, B] use >< instead
  trait CheckDistinct[A]
  trait GetSubscript[A, B]
  trait Reorder[A, B]

  trait DualExpression {
    type _OpStr
  }
  trait UnaryOn extends DualExpression {

    trait On[A] {}
  }
  trait BinaryOn extends DualExpression {

    trait On[A, B] {}
  }

  import singleton.ops.+

  trait AppendByName[O] extends UnaryOn {
    type _OpStr = "AppendByName[" + O + "]"
  }
  trait SquashByName[O] extends UnaryOn {
    type _OpStr = "SquashByName[" + O + "]"
  }
  trait DimensionWise[O] extends BinaryOn {
    type _OpStr = "DimensionWise[" + O + "]"
  }
}
