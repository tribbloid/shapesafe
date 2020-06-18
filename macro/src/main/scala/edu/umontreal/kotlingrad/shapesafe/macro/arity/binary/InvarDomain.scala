package edu.umontreal.kotlingrad.shapesafe.`macro`.arity.binary

import edu.umontreal.kotlingrad.shapesafe.`macro`.arity.Utils.Op
import edu.umontreal.kotlingrad.shapesafe.`macro`.arity.Proof.Invar
import edu.umontreal.kotlingrad.shapesafe.`macro`.arity.{Arity, Expr, Utils}
import shapeless.Lazy

import scala.language.higherKinds

class InvarDomain[N1, N2](
    val a1: Invar[N1],
    val a2: Invar[N2]
) {

  type A1 = Invar[N1]
  type A2 = Invar[N2]

  implicit val out1: Arity.Const[N1] = a1.out
  implicit val out2: Arity.Const[N2] = a2.out

  object Equal extends (A1 MayEqual A2) with Invar[N1] {

    override type Out = a1.Out
    override def out: Out = a1.out

//    implicitly[Out <:< Arity.Exact[N1]]
  }

  // Fr = Functor
  class Op2Impl[Fr[X, Y] <: Op]()(
      implicit outOp: Fr[N1, N2]
  ) extends Expr.Out_=[Arity.FromOp[Fr[N1, N2]]]()
      with Invar[Fr[N1, N2]]
      with Op2[A1, A2, Fr] {

//    implicitly[Out <:< Arity.Exact[Fr[N1, N2]]]
  }
}

object InvarDomain {

  implicit def summon[N1, N2](
      implicit
      a1: Invar[N1],
      a2: Invar[N2]
  ): InvarDomain[N1, N2] = new InvarDomain[N1, N2](a1, a2)

//  implicit def summon[N1, N2](
//      implicit
//      a1: Lazy[_ <: Invar[N1]],
//      a2: Lazy[_ <: Invar[N2]]
//  ): InvarDomain[N1, N2] = new InvarDomain[N1, N2](a1.value, a2.value)
}
