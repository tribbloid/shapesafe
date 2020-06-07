package edu.umontreal.kotlingrad.shapesafe

import edu.umontreal.kotlingrad.shapesafe.Arity.{OfInt, OfInt_Unsafe, OfSize}
import edu.umontreal.kotlingrad.shapesafe.Constraint.ElementOfType
import shapeless.{HList, Nat, ProductArgs}

class DoubleVector[A <: Arity](
    val arity: A,
    val data: List[Double]
) extends Serializable {}

object DoubleVector extends ProductArgs {

  def applyProduct[D <: HList, N <: Nat](data: D)(
      implicit proofOfSize: D OfSize N,
      proofOfType: D ElementOfType Double
  ): DoubleVector[OfSize[D, N]] = {

    val list = data.runtimeList.map { v =>
      v.asInstanceOf[Double]
    }

    new DoubleVector(proofOfSize, list)
  }

  @transient object from {

    def hList[D <: HList, N <: Nat](data: D)(
        implicit proofOfSize: D OfSize N,
        proofOfType: D ElementOfType Double
    ): DoubleVector[OfSize[D, N]] = applyProduct(data)(proofOfSize, proofOfType)
  }

  def zeros[Lit](w: Witness.Lt[Int]): DoubleVector[OfInt[w.T]] = {

    new DoubleVector(OfInt.safe(w), List.fill(w.value)(0.0))
  }

  @transient object unsafe {

    def zeros(number: Int): DoubleVector[OfInt_Unsafe] = {

      new DoubleVector(OfInt_Unsafe(number), List.fill(number)(0.0))
    }
  }
}
