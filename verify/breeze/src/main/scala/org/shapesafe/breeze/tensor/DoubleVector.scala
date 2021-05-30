package org.shapesafe.breeze.tensor

import breeze.linalg.DenseVector
import breeze.signal
import org.shapesafe.core.arity.Const.Literal
import org.shapesafe.core.arity.ProveArity.{|-, |-<}
import org.shapesafe.core.arity.nullary.SizeOf
import org.shapesafe.core.arity.{Arity, ArityAPI, LeafArity, Var}
import org.shapesafe.core.util.Constraint.ElementOfType
import shapeless.{HList, ProductArgs, Witness}

import scala.language.implicitConversions
import scala.util.Random

class DoubleVector[A1 <: Arity](
    val arity: ArityAPI.^[A1],
    val data: Vec[Double] // should support sparse/lazy vector
) extends Serializable {

  import org.shapesafe.core.arity.ops.ArityOps._

  // TODO: the format should be customisable
  override lazy val toString: String = {
    s"${arity.toString} \u00d7 1: Double"
  }

  def reify[O <: LeafArity](
      implicit
      prove: A1 |- O
  ): DoubleVector[O] = {

    val evaled = arity.eval
    new DoubleVector(evaled, data)
  }

  def dot_*[A2 <: Arity](that: DoubleVector[A2])(
      implicit
      proof: A1 ==! A2 |-< _
  ): Double = {

    val result: Double = this.data.dot(that.data)
    result
  }

  def concat[A2 <: Arity, O <: LeafArity](that: DoubleVector[A2])(
      implicit
      lemma: (A1 :+ A2) |- O
  ): DoubleVector[O] = { // TODO: always successful, can execute lazily without lemma

    val op = this.arity :+ that.arity
    val proof = lemma(op)

    val data = DenseVector.vertcat(this.data.toDenseVector, that.data.toDenseVector)

    new DoubleVector(proof.value.^, data)
  }

  def pad[O <: LeafArity](padding: Witness.Lt[Int])(
      implicit
      lemma: (A1 :+ (Literal[padding.T] :* Arity._2._Arity)) |- O
  ): DoubleVector[O] = {

    val _padding = Arity(padding)
    val op = this.arity :+ (_padding :* Arity._2)
    val proof = lemma(op)
    val out = proof.value.^

    val fill = DenseVector.fill(out.runtimeArity)(0.0)

    val dOut = DenseVector.vertcat(fill, this.data.toDenseVector, fill)

    new DoubleVector(out, dOut)
  }

  def conv[
      A2 <: Arity,
      O <: LeafArity
  ](
      kernel: DoubleVector[A2],
      stride: Witness.Lt[Int]
  )(
      implicit
      lemma: ((A1 :- A2 :+ Arity._1._Arity) :/ Literal[stride.T]) |- O
  ): DoubleVector[O] = {

    val _stride = Arity(stride)

    val op = (this.arity :- kernel.arity :+ Arity._1) :/ _stride
    val proof = lemma(op)
    val out = proof.value.^

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
      A2 <: Arity,
      O <: LeafArity
  ](
      kernel: DoubleVector[A2]
  )(
      implicit
      lemma: ((A1 :- A2 :+ Arity._1._Arity) :/ Arity._1._Arity) |- O
  ): DoubleVector[O] = {

    conv(kernel, 1)
  }
}

object DoubleVector extends ProductArgs {

  def applyProduct[D <: HList, O <: LeafArity](data: D)(
      implicit
      proofOfSize: SizeOf[D] |- O,
      proofOfType: D ElementOfType Double
  ): DoubleVector[O] = {

    val list = data.runtimeList.map { v =>
      v.asInstanceOf[Double]
    }

    val size = SizeOf(data)
    new DoubleVector(proofOfSize.valueOf(size).^, Vec.apply(list.toArray))
  }

  @transient object from {

    def hList[D <: HList, O <: LeafArity](data: D)(
        implicit
        proofOfSize: SizeOf[D] |- O,
        proofOfType: D ElementOfType Double
    ): DoubleVector[O] = {

      applyProduct(data)(proofOfSize, proofOfType)
    }
  }

  def zeros(lit: Witness.Lt[Int]): DoubleVector[Literal[lit.T]] = {

    new DoubleVector(Arity(lit), DenseVector.fill(lit.value)(0.0))
  }

  def random(lit: Witness.Lt[Int]): DoubleVector[Literal[lit.T]] = {
    val list = DenseVector.fill(lit.value) {
      Random.nextDouble()
    }

    new DoubleVector(Arity(lit), list)
  }

  @transient object unsafe {

    def zeros(number: Int): DoubleVector[Var] = {

      new DoubleVector(Var(number).^, DenseVector.fill(number)(0.0))
    }
  }

  case class Reified[A1 <: LeafArity](self: DoubleVector[A1]) {

    val arity = self.arity

    def crossValidate(): Unit = {

      arity.runtimeTry foreach { n =>
        n == self.data.size
      }
    }
  }

  implicit def asReified[A1 <: Arity, O <: LeafArity](v: DoubleVector[A1])(
      implicit
      prove: A1 |- O
  ): Reified[O] = {
    Reified(v.reify)
  }
}
