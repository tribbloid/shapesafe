package org.shapesafe.core.debugging

trait ExprLike {

  type Expr[T <: CanPeek] = T#_Expr // with T

  trait :<<-[A, B]
  trait ><[A, B]

  //  trait Infix[A, S, B] extends (A ~~ Only[S] ~~ B)
  //  trait PrefixW1[S, A] extends (Only[S] ~~ A)
  //  trait PrefixW2[S, A, B] extends (Only[S] ~~ A ~~ B)

  trait HasLiteral {
    type Lit
  }

  trait |<<-[A, B]
//  trait OuterProduct[A, B] use >< instead
  trait CheckDistinct[A]
  trait GetSubscript[A, B]
  trait Reorder[A, B]

  trait Unary extends HasLiteral {

    type On[A]
  }
  trait UnaryOn extends HasLiteral {

    trait On[A] {}
  }
  trait Binary extends HasLiteral {

    type On[A, B]
  }
  trait BinaryOn extends HasLiteral {

    trait On[A, B] {}
  }

  import singleton.ops.+

  trait AppendByName[O] extends UnaryOn {
    type Lit = "AppendByName[" + O + "]"
  }
  trait SquashByName[O] extends UnaryOn {
    type Lit = "SquashByName[" + O + "]"
  }
  trait DimensionWise[O] extends BinaryOn {
    type Lit = "DimensionWise[" + O + "]"
  }
}
