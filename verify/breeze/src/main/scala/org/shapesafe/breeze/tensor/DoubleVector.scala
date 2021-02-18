package org.shapesafe.breeze.tensor

import breeze.linalg.DenseVector
import breeze.signal
import org.shapesafe.core.arity.ProveArity.~~>
import org.shapesafe.core.arity.Utils.NatAsOp
import org.shapesafe.core.arity.nullary.SizeOf
import org.shapesafe.core.arity.{Arity, LeafArity}
import org.shapesafe.djl.tensor.Const.VecShape
import org.shapesafe.core.util.Constraint.ElementOfType
import shapeless.{HList, ProductArgs, Witness}

import scala.language.implicitConversions
import scala.util.Random

class DoubleVector[A1 <: VecShape](
    val shape: A1,
    val data: Vec[Double] // should support sparse/lazy vector
) extends Serializable {

  import LeafArity._
  import org.shapesafe.core.arity.ops.ArityOps._

  // TODO: the format should be customisable
  override lazy val toString: String = {
    s"${shape.valueStr} \u00d7 1: Double"
  }

  def reify[O <: LeafArity](
      implicit
      prove: A1 ~~> O
  ): DoubleVector[O] = {

    val proof = prove(shape)
    val out = proof.out
    new DoubleVector(out, data)
  }

  def dot_*[A2 <: LeafArity](that: DoubleVector[A2])(
      implicit
      proof: A1 =!= A2 ~~> _
  ): Double = {

    val result: Double = this.data.dot(that.data)

    result
  }

  def concat[A2 <: VecShape, O <: LeafArity](that: DoubleVector[A2])(
      implicit
      lemma: (A1 :+ A2) ~~> O
  ): DoubleVector[O] = { // TODO: always successful, can execute lazily without lemma

    val op = this.shape :+ that.shape
    val proof = lemma(op)

    val data = DenseVector.vertcat(this.data.toDenseVector, that.data.toDenseVector)

    new DoubleVector(proof.out, data)
  }

  def pad[O <: LeafArity](padding: Witness.Lt[Int])(
      implicit
      lemma: (A1 :+ (Literal[padding.T] :* LeafArity.Wide._2.Wide)) ~~> O
  ): DoubleVector[O] = {

    val _padding = Arity(padding)
    val op = this.shape :+ (_padding :* LeafArity._2)
    val proof = lemma(op)
    val out = proof.out

    val fill = DenseVector.fill(out.runtime)(0.0)

    val dOut = DenseVector.vertcat(fill, this.data.toDenseVector, fill)

    new DoubleVector(out, dOut)
  }

  def conv[
      A2 <: VecShape,
      O <: LeafArity
  ](
      kernel: DoubleVector[A2],
      stride: Witness.Lt[Int]
  )(
      implicit
      lemma: ((A1 :- A2 :+ LeafArity.Wide._1.Wide) :/ Literal[stride.T]) ~~> O
  ): DoubleVector[O] = {

    val _stride: Literal[stride.T] = Arity(stride)

    val op = (this.shape :- kernel.shape :+ LeafArity._1) :/ _stride
    val proof = lemma(op)
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
      A2 <: VecShape,
      O <: LeafArity
  ](
      kernel: DoubleVector[A2]
  )(
      implicit
      lemma: ((A1 :- A2 :+ LeafArity.Wide._1.Wide) :/ LeafArity.Wide._1.Wide) ~~> O
  ): DoubleVector[O] = {

    conv(kernel, 1)
  }
}

object DoubleVector extends ProductArgs {

  import LeafArity._

  def applyProduct[D <: HList, S](data: D)(
      implicit
      proofOfSize: SizeOf[D] ~~> Const[S],
      proofOfType: D ElementOfType Double
  ): DoubleVector[Const[S]] = {

    val list = data.runtimeList.map { v =>
      v.asInstanceOf[Double]
    }

    val size = SizeOf(data)
    new DoubleVector(proofOfSize.valueOf(size), Vec.apply(list.toArray))
  }

  @transient object from {

    def hList[D <: HList, S <: NatAsOp[_]](data: D)(
        implicit
        proofOfSize: SizeOf[D] ~~> Const[S],
        proofOfType: D ElementOfType Double
    ): DoubleVector[Const[S]] = {

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

      new DoubleVector(Var(number), DenseVector.fill(number)(0.0))
    }
  }

  case class Reified[A1 <: LeafArity](self: DoubleVector[A1]) {

    val arity: A1 = self.shape

    def crossValidate(): Unit = {

      arity.runtimeOpt foreach { n =>
        n == self.data.size
      }
    }
  }

  implicit def asReified[A1 <: VecShape, O <: LeafArity](v: DoubleVector[A1])(
      implicit
      prove: A1 ~~> O
  ): Reified[O] = {
    Reified(v.reify)
  }
}
