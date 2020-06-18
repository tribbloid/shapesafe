package edu.umontreal.kotlingrad.shapesafe.core.tensor

import edu.umontreal.kotlingrad.shapesafe.common.arity.Arity
import edu.umontreal.kotlingrad.shapesafe.common.arity.Const.NatAsOp
import edu.umontreal.kotlingrad.shapesafe.common.arity.binary.MayEqual
import edu.umontreal.kotlingrad.shapesafe.common.arity.binary.Op2.Arity_+
import edu.umontreal.kotlingrad.shapesafe.common.arity.nullary.OfSize
import edu.umontreal.kotlingrad.shapesafe.common.util.Constraint.ElementOfType
import shapeless.{HList, ProductArgs, Witness}

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
      implicit op: A Arity_+ A2
  ): DoubleVector[op.Out] = {

    val data = this.data ++ that.data

    new DoubleVector(op.Out, data)
  }
}

object DoubleVector extends ProductArgs {

  import Arity._

  def applyProduct[D <: HList, S <: NatAsOp[_]](data: D)(
      implicit
      proofOfSize: D OfSize S,
      proofOfType: D ElementOfType Double
  ): DoubleVector[proofOfSize.Out] = {

    val list = data.runtimeList.map { v =>
      v.asInstanceOf[Double]
    }

    new DoubleVector(proofOfSize.Out, list)
  }

  @transient object from {

    def hList[D <: HList, S <: NatAsOp[_]](data: D)(
        implicit proofOfSize: D OfSize S,
        proofOfType: D ElementOfType Double
    ): DoubleVector[proofOfSize.Out] = {

      applyProduct(data)(proofOfSize, proofOfType)
    }
  }

  def zeros[Lit](lit: Witness.Lt[Int]): DoubleVector[FromLiteral[lit.T]] = {

    new DoubleVector(FromLiteral.apply(lit), List.fill(lit.value)(0.0))
  }

  def random[Lit](lit: Witness.Lt[Int]): DoubleVector[FromLiteral[lit.T]] = {
    val list = List.fill(lit.value) {
      Random.nextDouble()
    }

    new DoubleVector(FromLiteral.apply(lit), list)
  }

  @transient object unsafe {

    def zeros(number: Int): DoubleVector[Unknown.type] = {

      new DoubleVector(Unknown, List.fill(number)(0.0))
    }
  }
}
