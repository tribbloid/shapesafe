package edu.umontreal.kotlingrad.shapesafe.tensor

import edu.umontreal.kotlingrad.shapesafe.util.Arity
import edu.umontreal.kotlingrad.shapesafe.util.Arity.{FromInt, FromInt_Unsafe, FromSize}
import edu.umontreal.kotlingrad.shapesafe.util.ArityOps.{CanSum, MayEqual, OfSize}
import edu.umontreal.kotlingrad.shapesafe.util.Constraint.ElementOfType
import shapeless.{HList, Nat, ProductArgs, Witness}

import scala.util.Random

class DoubleVector[A <: Arity](
    val arity: A,
    val data: Seq[Double] // should support sparse/lazy vector
) extends Serializable {

  {
    require(arity.number == data.size)
  }

  // TODO: the format should be customisable
  override lazy val toString: String = {
    s"${arity.number} \u00d7 1: Double"
  }

  def dot_*[A2 <: Arity](that: DoubleVector[A2])(implicit proof: A MayEqual A2): Double = {

    proof.yieldRT(this.arity, that.arity) // run-time check

    val result: Double = this.data
      .zip(that.data)
      .map {
        case (v1, v2) =>
          v1 * v2
      }
      .sum

    result
  }

  def concat[A2 <: Arity](that: DoubleVector[A2])(implicit op: A CanSum A2): DoubleVector[op.Yield] = {

    val arity = op.yieldRT(this.arity, that.arity)

    val data = this.data ++ that.data

    new DoubleVector[op.Yield](arity, data)
  }

}

object DoubleVector extends ProductArgs {

  def applyProduct[D <: HList, N <: Nat](data: D)(
      implicit proofOfSize: D OfSize N,
      proofOfType: D ElementOfType Double
  ): DoubleVector[FromSize[proofOfSize._N]] = {

    val list = data.runtimeList.map { v =>
      v.asInstanceOf[Double]
    }

    new DoubleVector(proofOfSize.yieldRT, list)
  }

  @transient object from {

    def hList[D <: HList, N <: Nat](data: D)(
        implicit proofOfSize: D OfSize N,
        proofOfType: D ElementOfType Double
    ): DoubleVector[FromSize[proofOfSize._N]] = applyProduct(data)(proofOfSize, proofOfType)
  }

  def zeros[Lit](lit: Witness.Lt[Int]): DoubleVector[FromInt[lit.T]] = {

    new DoubleVector(FromInt.safe(lit), List.fill(lit.value)(0.0))
  }

  def random[Lit](lit: Witness.Lt[Int]): DoubleVector[FromInt[lit.T]] = {
    val list = List.fill(lit.value)(0.0).map { _ =>
      Random.nextDouble()
    }

    new DoubleVector(FromInt.safe(lit), list)
  }

  @transient object unsafe {

    def zeros(number: Int): DoubleVector[FromInt_Unsafe] = {

      new DoubleVector(FromInt_Unsafe(number), List.fill(number)(0.0))
    }
  }
}
