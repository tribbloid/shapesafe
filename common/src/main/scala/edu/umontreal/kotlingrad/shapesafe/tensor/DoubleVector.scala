package edu.umontreal.kotlingrad.shapesafe.tensor

import edu.umontreal.kotlingrad.shapesafe.arity.Arity
import edu.umontreal.kotlingrad.shapesafe.arity.binary.MayEqual
import edu.umontreal.kotlingrad.shapesafe.arity.binary.Op2.+
import edu.umontreal.kotlingrad.shapesafe.arity.nullary.OfSize
import edu.umontreal.kotlingrad.shapesafe.util.Constraint.ElementOfType
import shapeless.{HList, Nat, ProductArgs, Witness}

import scala.util.Random

class DoubleVector[A <: Arity](
    val arity: A,
    val data: Seq[Double] // should support sparse/lazy vector
) extends Serializable {

  {
    crossValidate()
  }

  def crossValidate(): Unit = {

    arity.numberOpt foreach { n =>
      n == data.size
    }
  }

  // TODO: the format should be customisable
  override lazy val toString: String = {
    s"${arity.valueStr} \u00d7 1: Double"
  }

  def dot_*[A2 <: Arity](that: DoubleVector[A2])(
      implicit proof: A MayEqual A2
  ): Double = {

    val result: Double = this.data
      .zip(that.data)
      .map {
        case (v1, v2) =>
          v1 * v2
      }
      .sum

    result
  }

  def concat[A2 <: Arity](that: DoubleVector[A2])(
      implicit op: A + A2
  ): DoubleVector[op.Out] = {

    val arity = implicitly[op.Out]

    val data = this.data ++ that.data

    new DoubleVector[op.Out](arity, data)
  }

}

object DoubleVector extends ProductArgs {

  import Arity._

  def applyProduct[D <: HList, N <: Nat](data: D)(
      implicit proofOfSize: D OfSize N,
      proofOfType: D ElementOfType Double
  ): DoubleVector[FromSize[proofOfSize.Out]] = {

    val list = data.runtimeList.map { v =>
      v.asInstanceOf[Double]
    }

    val arity = implicitly[proofOfSize.Out]

    new DoubleVector(arity, list)
  }

  @transient object from {

    def hList[D <: HList, N <: Nat](data: D)(
        implicit proofOfSize: D OfSize N,
        proofOfType: D ElementOfType Double
    ): DoubleVector[FromSize[proofOfSize.Out]] = {

      applyProduct(data)(proofOfSize, proofOfType)
    }
  }

  def zeros[Lit](lit: Witness.Lt[Int]): DoubleVector[FromLiteral[lit.T]] = {

    new DoubleVector(FromLiteral.create(lit), List.fill(lit.value)(0.0))
  }

  def random[Lit](lit: Witness.Lt[Int]): DoubleVector[FromLiteral[lit.T]] = {
    val list = List.fill(lit.value) {
      Random.nextDouble()
    }

    new DoubleVector(FromLiteral.create(lit), list)
  }

  @transient object unsafe {

    def zeros(number: Int): DoubleVector[Unknown.type] = {

      new DoubleVector(Unknown, List.fill(number)(0.0))
    }
  }
}
