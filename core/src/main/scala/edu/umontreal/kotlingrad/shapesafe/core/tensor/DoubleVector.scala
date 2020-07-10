package edu.umontreal.kotlingrad.shapesafe.core.tensor

import breeze.linalg.DenseVector
import breeze.signal
import edu.umontreal.kotlingrad.shapesafe.m.arity.Utils.NatAsOp
import edu.umontreal.kotlingrad.shapesafe.m.arity.binary.MayEqual
import edu.umontreal.kotlingrad.shapesafe.m.arity.nullary.OfSize
import edu.umontreal.kotlingrad.shapesafe.m.arity.{Arity, Implies, Proof}
import edu.umontreal.kotlingrad.shapesafe.m.util.Constraint.ElementOfType
import shapeless.{HList, ProductArgs, Witness}

import scala.util.Random

class DoubleVector[A1 <: Shape](
    val shape: A1,
    val data: Vec[Double] // should support sparse/lazy vector
) extends Serializable {

  import Arity._
  import edu.umontreal.kotlingrad.shapesafe.m.arity.DSL._

  // TODO: the format should be customisable
  override lazy val toString: String = {
    s"${shape.valueStr} \u00d7 1: Double"
  }

  def dot_*[A2 <: Arity](that: DoubleVector[A2])(
      implicit
      proof: A1 MayEqual A2 Implies Proof
  ): Double = {

    val result: Double = this.data.dot(that.data)

    result
  }

  def concat[A2 <: Shape, P <: Proof](that: DoubleVector[A2])(
      implicit
      lemma: (A1 Plus A2) Implies P
  ): DoubleVector[P#Out] = { // TODO: always succesful, can execute lazily without lemma

    val op = this.shape + that.shape
    val proof: P = lemma(op)

    val data = DenseVector.vertcat(this.data.toDenseVector, that.data.toDenseVector)

    new DoubleVector(proof.out, data)
  }

  def pad[T](padding: Witness.Lt[Int]): DoubleVector[A1 Plus (FromLiteral[padding.T] Times Arity._2.WideType)] = {

    val _padding: FromLiteral[padding.T] = Arity(padding)

    val op = this.shape + (_padding * Arity._2.value)

    val fill = DenseVector.fill(padding.value)(0.0)

    val dOut = DenseVector.vertcat(fill, this.data.toDenseVector, fill)

    new DoubleVector(op, dOut)
  }

  def conv[
      A2 <: Shape,
      P <: Proof
  ](
      kernel: DoubleVector[A2],
      stride: Witness.Lt[Int]
  )(
      implicit lemma: ((A1 Minus A2 Plus Arity._1.WideType) DividedBy FromLiteral[stride.T]) Implies P
  ): DoubleVector[P#Out] = {

    val _stride: FromLiteral[stride.T] = Arity(stride)

    val op = (this.shape - kernel.shape + Arity._1.value) / _stride
    val proof: P = lemma(op)

//    for (padding = 0.to(that.data.size - this.data.size))
    val dOut: DenseVector[Double] = signal.convolve(this.data.toDenseVector, kernel.data.toDenseVector)

    new DoubleVector(proof.out, dOut)
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

  implicit class ReifiedView[A1 <: Shape, P <: Proof](self: DoubleVector[A1])(implicit prove: A1 Implies P) {

    val arity: P#Out = {

      val proof = prove.apply(self.shape)
      proof.out
    }

    lazy val reify: DoubleVector[P#Out] = {

      new DoubleVector(arity, self.data)
    }

    def crossValidate(): Unit = {

      arity.numberOpt foreach { n =>
        n == self.data.size
      }
    }
  }
}
