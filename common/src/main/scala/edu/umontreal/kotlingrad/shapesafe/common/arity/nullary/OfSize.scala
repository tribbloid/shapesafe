package edu.umontreal.kotlingrad.shapesafe.common.arity.nullary

import edu.umontreal.kotlingrad.shapesafe.common.arity.Arity.FromSize
import edu.umontreal.kotlingrad.shapesafe.common.arity.Expr
import shapeless.ops.hlist
import shapeless.{HList, Nat}
import singleton.ops.ToInt

class OfSize[Data <: HList, S <: ToInt[_]]()(
    implicit op: S
) extends Expr.Out[FromSize[S]] {}

object OfSize {

//  type ToIntSafe[S] = ToInt[S] { type Out <: Int }

  implicit def observe[Data <: HList, N <: Nat](
      implicit
      getSize: hlist.Length.Aux[Data, N],
      op: ToInt[N]
  ): OfSize[Data, ToInt[N]] = {

    new OfSize[Data, ToInt[N]]()
  }
}
