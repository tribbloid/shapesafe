package edu.umontreal.kotlingrad.shapesafe.core.tensor

import edu.umontreal.kotlingrad.shapesafe.m.arity.Arity.{FromLiteral, Unknown}
import edu.umontreal.kotlingrad.shapesafe.m.arity.Utils.NatAsOp
import edu.umontreal.kotlingrad.shapesafe.m.arity.binary.MayEqual
import edu.umontreal.kotlingrad.shapesafe.m.arity.nullary.OfSize
import edu.umontreal.kotlingrad.shapesafe.m.arity.{Arity, Implies, Proof}
import edu.umontreal.kotlingrad.shapesafe.m.util.Constraint.ElementOfType
import shapeless.{HList, ProductArgs, Witness}

import scala.util.Random

class DoubleVector[X <: Back](
    val arity: X,
    val data: Seq[Double] // should support sparse/lazy vector
) extends Serializable {

  import edu.umontreal.kotlingrad.shapesafe.m.arity.DSL._
//  {
//    crossValidate()
//  }

//  def crossValidate(): Unit = {
//
//    arity.numberOpt foreach { n =>
//      n == data.size
//    }
//  }

  // TODO: the format should be customisable
//  override lazy val toString: String = {
//    s"${arity.valueStr} \u00d7 1: Double"
//  }

  def dot_*[Y <: Arity](that: DoubleVector[Y])(
      implicit
      proof: (X MayEqual Y) Implies Proof
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

  def concat[Y <: Back, P <: Proof](that: DoubleVector[Y])(
      implicit prove: (X + Y) Implies P
  ): DoubleVector[P#Out] = { // TODO: how to add type annotation?

    val op = this.arity + that.arity
    val proof: P = prove(op)

    val data = this.data ++ that.data

    new DoubleVector(proof.out, data)
  }
}

object DoubleVector extends ProductArgs {

  def applyProduct[D <: HList, S <: NatAsOp[_]](data: D)(
      implicit
      proofOfSize: D OfSize S,
      proofOfType: D ElementOfType Double
  ): DoubleVector[proofOfSize.Out] = {

    val list = data.runtimeList.map { v =>
      v.asInstanceOf[Double]
    }

    new DoubleVector(proofOfSize.out, list)
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

    new DoubleVector(Arity(lit), List.fill(lit.value)(0.0))
  }

  def random[Lit](lit: Witness.Lt[Int]): DoubleVector[FromLiteral[lit.T]] = {
    val list = List.fill(lit.value) {
      Random.nextDouble()
    }

    new DoubleVector(Arity(lit), list)
  }

  @transient object unsafe {

    def zeros(number: Int): DoubleVector[Unknown.type] = {

      new DoubleVector(Unknown, List.fill(number)(0.0))
    }
  }
}
