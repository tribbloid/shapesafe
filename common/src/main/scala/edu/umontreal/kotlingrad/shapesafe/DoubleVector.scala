package edu.umontreal.kotlingrad.shapesafe

import Arity.OfSize
import edu.umontreal.kotlingrad.shapesafe.Constraint.OfElementType
import shapeless.{HList, Nat}

case class DoubleVector[A <: Arity](
    arity: A,
    data: List[Double]
) {}

object DoubleVector {

  def fromHList[D <: HList, N <: Nat](data: D)(
      implicit proofOfSize: D OfSize N,
      proofOfType: D OfElementType Double
  ): DoubleVector[OfSize[D, N]] = {

    val list = data.runtimeList.map { v =>
      v.asInstanceOf[Double]
    }

    DoubleVector(proofOfSize, list)
  }

  //TODO:
  // def fromTuple ...
  // support for tuple larger than 22 is broken, need better solutions, codegen / macro / meta ?
}
