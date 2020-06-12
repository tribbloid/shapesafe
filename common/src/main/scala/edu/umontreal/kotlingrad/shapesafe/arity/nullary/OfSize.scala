package edu.umontreal.kotlingrad.shapesafe.arity.nullary

import edu.umontreal.kotlingrad.shapesafe.arity.Arity.FromSize
import edu.umontreal.kotlingrad.shapesafe.arity.ArityOp
import shapeless.ops.hlist
import shapeless.{HList, Nat}
import singleton.ops.impl.Op

class OfSize[Data <: HList, S <: Int with Op]()(implicit number: S) extends ArityOp {

  type Out = FromSize[S]
  val Out: FromSize[S] = implicitly[FromSize[S]]
}

object OfSize {

  implicit def summon[Data <: HList, N <: Nat](
      implicit
      getSize: hlist.Length.Aux[Data, N],
      nat2Op: singleton.ops.ToInt[N]
  ): OfSize[Data, singleton.ops.ToInt[N]] = {

    new OfSize[Data, singleton.ops.ToInt[N]]()
  }
}
