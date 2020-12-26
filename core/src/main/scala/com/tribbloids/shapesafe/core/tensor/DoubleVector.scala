package com.tribbloids.shapesafe.core.tensor

import breeze.linalg.DenseVector
import breeze.signal
import com.tribbloids.shapesafe.m.arity.Utils.NatAsOp
import com.tribbloids.shapesafe.m.arity.binary.MayEqual
import com.tribbloids.shapesafe.m.arity.nullary.OfSize
import com.tribbloids.shapesafe.m.arity.{Arity, Proof}
import com.tribbloids.shapesafe.m.util.Constraint.ElementOfType
import com.tribbloids.shapesafe.m.~~>
import shapeless.{HList, ProductArgs, Witness}

import scala.util.Random

import scala.language.implicitConversions

class DoubleVector[A1 <: Shape](
    val shape: A1,
    val data: Vec[Double] // should support sparse/lazy vector
) extends Serializable {

  import Arity._
  import com.tribbloids.shapesafe.m.arity.DSL._

  // TODO: the format should be customisable
  override lazy val toString: String = {
    s"${shape.valueStr} \u00d7 1: Double"
  }

  def reify[P <: Proof](implicit prove: A1 ~~> P): DoubleVector[P#Out] = {

    val proof = prove(shape)
    val out = proof.out
    new DoubleVector(out, data)
  }

  def dot_*[A2 <: Arity](that: DoubleVector[A2])(
      implicit
      proof: A1 MayEqual A2 ~~> Proof
  ): Double = {

    val result: Double = this.data.dot(that.data)

    result
  }

  def concat[A2 <: Shape, P <: Proof](that: DoubleVector[A2])(
      implicit
      lemma: (A1 T_+ A2) ~~> P
  ): DoubleVector[P#Out] = { // TODO: always succesful, can execute lazily without lemma

    val op = this.shape + that.shape
    val proof: P = lemma(op)

    val data = DenseVector.vertcat(this.data.toDenseVector, that.data.toDenseVector)

    new DoubleVector(proof.out, data)
  }

  def pad[P <: Proof](padding: Witness.Lt[Int])(
      implicit
      lemma: (A1 T_+ (FromLiteral[padding.T] T_* Arity._2.Wide)) ~~> P
  ): DoubleVector[P#Out] = {

    val _padding = Arity(padding)
    val op = this.shape + (_padding * Arity._2.value)
    val proof: P = lemma(op)
    val out = proof.out

    val fill = DenseVector.fill(out.number)(0.0)

    val dOut = DenseVector.vertcat(fill, this.data.toDenseVector, fill)

    new DoubleVector(out, dOut)
  }

  def conv[
      A2 <: Shape,
      P <: Proof
  ](
      kernel: DoubleVector[A2],
      stride: Witness.Lt[Int]
  )(
      implicit
      lemma: ((A1 T_- A2 T_+ Arity._1.Wide) T_/ FromLiteral[stride.T]) ~~> P
  ): DoubleVector[P#Out] = {

    val _stride: FromLiteral[stride.T] = Arity(stride)

    val op = (this.shape - kernel.shape + Arity._1.value) / _stride
    val proof: P = lemma(op)
    val out = proof.out

    val range = 0.to(this.data.size - kernel.data.size, stride.value)

//    for (padding = 0.to(that.data.size - this.data.size))
    val dOut: DenseVector[Double] = signal.convolve(
      this.data.toDenseVector,
      kernel.data.toDenseVector,
      range
    )

    new DoubleVector(out, dOut)
  }

  def conv[
      A2 <: Shape,
      P <: Proof
  ](
      kernel: DoubleVector[A2]
  )(
      implicit
      lemma: ((A1 T_- A2 T_+ Arity._1.Wide) T_/ Arity._1.Wide) ~~> P
  ): DoubleVector[P#Out] = {

    conv(kernel, 1)
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

    new DoubleVector(proofOfSize.out, Vec.apply(list.toArray))
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

    new DoubleVector(Arity(lit), DenseVector.fill(lit.value)(0.0))
  }

  def random[Lit](lit: Witness.Lt[Int]): DoubleVector[FromLiteral[lit.T]] = {
    val list = DenseVector.fill(lit.value) {
      Random.nextDouble()
    }

    new DoubleVector(Arity(lit), list)
  }

  @transient object unsafe {

    def zeros(number: Int): DoubleVector[Var] = {

      new DoubleVector(Var(number), DenseVector.fill(number)(0.0))
    }
  }

  case class Reified[A1 <: Arity](self: DoubleVector[A1]) {

    val arity: A1 = self.shape

    def crossValidate(): Unit = {

      arity.numberOpt foreach { n =>
        n == self.data.size
      }
    }
  }

  implicit def asReified[A1 <: Shape, P <: Proof](v: DoubleVector[A1])(
      implicit prove: A1 ~~> P
  ): Reified[P#Out] = {
    Reified(v.reify)
  }
}
