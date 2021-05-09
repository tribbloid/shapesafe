package org.shapesafe.core.debugging

import singleton.ops.+

trait ExprLike {

  type Expr[T <: CanPeek] = T#_Expr with T

  trait ?

  trait :<<-[A, B]
  trait ><[A, B]

  //  trait Infix[A, S, B] extends (A ~~ Only[S] ~~ B)
  //  trait PrefixW1[S, A] extends (Only[S] ~~ A)
  //  trait PrefixW2[S, A, B] extends (Only[S] ~~ A ~~ B)

  trait HasLiteral {
    type Lit
  }

  //  trait Infix[A, B] extends HasLiteral {}
  //
  //  trait PrefixW1[A] extends HasLiteral {
  //
  //    type OpsOut = Lit + A
  //  }

  object Elementary {
    trait +[A, B] extends HasLiteral {
      type Lit = " + "
    }
    trait -[A, B] extends HasLiteral {
      type Lit = " - "
    }
    trait *[A, B] extends HasLiteral {
      type Lit = " * "
    }
    trait /[A, B] extends HasLiteral {
      type Lit = " / "
    }
    trait ==[A, B] extends HasLiteral {
      type Lit = " == "
    }
  }

  trait |<<-[A, B]
  trait OuterProduct[A, B]
  trait CheckDistinct[A]
  trait GetSubscript[A, B]
  trait Reorder[A, B]

  trait Unary extends HasLiteral {

    trait On[A] extends HasLiteral {
      type Lit = Unary.this.Lit + "On"
    }
  }

  trait Binary extends HasLiteral {

    trait On[A, B] extends HasLiteral {}
  }

  trait AppendByName[O <: HasLiteral] extends Unary {
    type Lit = "AppendByName[" + O#Lit + "]"
  }
  trait SquashByName[O <: HasLiteral] extends Unary {
    type Lit = "SquashByName[" + O#Lit + "]"
  }
  trait DimensionWise[O <: HasLiteral] extends Binary {
    type Lit = "DimensionWise[" + O#Lit + "]"
  }
}
