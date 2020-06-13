package edu.umontreal.kotlingrad.shapesafe.core.tensor

import edu.umontreal.kotlingrad.shapesafe.common.arity.binary.MayEqual
import edu.umontreal.kotlingrad.shapesafe.common.arity.binary.Op2.+
import edu.umontreal.kotlingrad.shapesafe.common.arity.nullary.OfSize
import edu.umontreal.kotlingrad.shapesafe.common.arity.{Arity, ArityLike, IntOp}
import edu.umontreal.kotlingrad.shapesafe.common.util.Constraint.ElementOfType
import shapeless.{HList, ProductArgs, Witness}

import scala.util.Random

class DoubleVector[A <: ArityLike](
    val arityLike: A,
    val data: Seq[Double] // should support sparse/lazy vector
) extends Serializable {

  {
    crossValidate()
  }

  implicit val arity: arityLike.Out = arityLike.Out

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
  ): DoubleVector[A + A2] = {

    val data = this.data ++ that.data

    new DoubleVector(op, data)
  }

}

object DoubleVector extends ProductArgs {

  import Arity._

  def applyProduct[D <: HList, S <: IntOp](data: D)(
      implicit proofOfSize: D OfSize S,
      proofOfType: D ElementOfType Double
  ): DoubleVector[D OfSize S] = {

    val list = data.runtimeList.map { v =>
      v.asInstanceOf[Double]
    }

    new DoubleVector(proofOfSize, list)
  }

  @transient object from {

    def hList[D <: HList, S <: IntOp](data: D)(
        implicit proofOfSize: D OfSize S,
        proofOfType: D ElementOfType Double
    ): DoubleVector[D OfSize S] = {

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
