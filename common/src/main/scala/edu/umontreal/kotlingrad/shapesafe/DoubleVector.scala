package edu.umontreal.kotlingrad.shapesafe

import Arity.{FromLiteral, OfSize}
import edu.umontreal.kotlingrad.shapesafe.Constraint.ElementOfType
import shapeless.{HList, Nat}

case class DoubleVector[A <: Arity](
    arity: A,
    data: List[Double]
) {}

object DoubleVector {

  def fromHList[D <: HList, N <: Nat](data: D)(
      implicit proofOfSize: D OfSize N,
      proofOfType: D ElementOfType Double
  ): DoubleVector[OfSize[D, N]] = {

    val list = data.runtimeList.map { v =>
      v.asInstanceOf[Double]
    }

    DoubleVector(proofOfSize, list)
  }

  //TODO:
  // def fromTuple ...
  // support for tuple larger than 22 is broken, need better solutions, codegen / macro / meta ?

  def zeros[Lit](w: W.Lt[Int]): DoubleVector[FromLiteral[w.T]] = {

    DoubleVector(FromLiteral.make(w), List.fill(w.value)(0.0))
  }
}
