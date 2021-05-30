package org.shapesafe.core.shape

import org.shapesafe.core.arity.ops.ArityOps
import org.shapesafe.core.arity.{Arity, Const, LeafArity}
import org.shapesafe.core.shape.LeafShape.><^
import org.shapesafe.core.shape.ProveShape.|-
import org.shapesafe.core.shape.binary.OuterProduct
import org.shapesafe.core.shape.ops.{EinSumOps, LeafOps, MatrixOps, VectorOps}
import org.shapesafe.core.shape.unary._
import shapeless.ops.hlist.Reverse
import shapeless.ops.nat.ToInt
import shapeless.{HList, Nat, SingletonProductArgs, Witness}

import scala.language.implicitConversions

trait ShapeAPI extends VectorOps with MatrixOps {

  import ShapeAPI._

  final override def toString: String = shape.toString

  final def verify[
      O <: Shape
  ](
      implicit
      prove: _Shape |- O
  ): ^[O] = prove.valueOf(shape).^

//  final def simplify[ // TODO: no use at the moment
//      O <: LeafShape
//  ](
//      implicit
//      prove: _Shape |- O
//  ): ^[O] = prove.valueOf(shape).^

  def eval[ // TODO: eval each member?
      O <: LeafShape
  ](
      implicit
      prove: _Shape |- O
  ): ^[O] = verify(prove)

  def peek(
      implicit
      reporter: ShapeReporters.PeekShape.Case[_Shape]
  ): this.type = this

  def interrupt(
      implicit
      reporter: ShapeReporters.InterruptShape.Case[_Shape]
  ): this.type = this

  def reason[
      O <: LeafShape
  ](
      implicit
      reporter: ShapeReporters.PeekShape.Case[_Shape],
      prove: _Shape |- O
  ): ^[O] = eval(prove)

  /**
    * assign new names
    */
  object namedWith {

    def apply[N <: Names](newNames: N): ^[|<<-[_Shape, N]] = {

      new |<<-[_Shape, N](shape.^, newNames).^
    }
  }

  lazy val |<<- : namedWith.type = namedWith

  // no need for Names constructor
  object named extends SingletonProductArgs {

    def applyProduct[H1 <: HList, H2 <: HList](
        v: H1
    )(
        implicit
        reverse: Reverse.Aux[H1, H2],
        lemma: Names.FromLiterals.Case[H2]
    ): ^[|<<-[_Shape, lemma.Out]] = {

      val out = lemma.apply(reverse(v))

      namedWith.apply(out)
    }
  }

  lazy val |<<-* : named.type = named

  object >< {

    def apply(
        that: ShapeAPI
    ): ^[OuterProduct[_Shape, that._Shape]] = {

      OuterProduct(shape, that.shape).^
    }

    // TODO: redundant?
//    def apply[THAT <: Shape](
//        that: ^[THAT]
//    ): ^[OuterProduct[_Shape, that._Shape]] = {
//
//      OuterProduct(shape, that.shape).^
//    }
  }

  def outer: ><.type = ><

  def einSum: EinSumOps[_Shape] = EinSumOps(shape)

  def contract[N <: Names](names: N): ^[Reorder[CheckEinSum[_Shape], N]] = {

    einSum.-->(names)
  }

  object Sub {

    def apply[T <: Index](v: T): ^[GetSubscript[_Shape, T]] = {
      GetSubscript(shape, v).^
    }

    def apply(i: Nat)(
        implicit
        toIntN: ToInt[i.N]
    ): ^[GetSubscript[_Shape, Index.I_th[i.N]]] = {

      apply(Index.I_th(i))
    }

    def apply(w: Witness.Lt[String]): ^[GetSubscript[_Shape, Index.Name[w.T]]] = {

      apply(Index.Name(w))
    }
  }

  def flattenWith(
      infix: ArityOps.Infix,
      that: ShapeAPI
  ): ^[infix._SquashByName.On[OuterProduct[_Shape, that._Shape]]] = {

    val outerP = ><(that)
    infix._SquashByName.On(outerP).^
  }

  def flatten(
      infix: ArityOps.Infix
  ): ^[infix._SquashByName.On[_Shape]] = {

    infix._SquashByName.On(this).^
  }

  def transposeWith[N <: Names](names: N): ^[Reorder[CheckDistinct[_Shape], N]] = {

    val distinct = CheckDistinct(shape)
    val result = Reorder(distinct, names)

    result.^
  }

  object transpose extends SingletonProductArgs {

    def applyProduct[H1 <: HList, H2 <: HList](
        v: H1
    )(
        implicit
        reverse: Reverse.Aux[H1, H2],
        lemma: Names.FromLiterals.Case[H2]
    ): ^[Reorder[CheckDistinct[_Shape], lemma.Out]] = {

      val out = lemma.apply(reverse(v))

      transposeWith(out)
    }
  }

  def dimensionWise(
      infix: ArityOps.Infix,
      that: ShapeAPI
  ): ^[infix._DimensionWise.On[_Shape, that._Shape]] = {

    infix._DimensionWise.On(this, that).^
  }

  def elementWise(
      that: ShapeAPI
  ): ^[ArityOps.==!._DimensionWise.On[_Shape, that._Shape]] = dimensionWise(ArityOps.==!, that)
}

object ShapeAPI {

  type Aux[T] = ShapeAPI { type _Shape = T }

  implicit def unbox[S <: Shape](v: Aux[S]): S = v.shape

  implicit def fromIntS[T <: Int with Singleton](v: T)(
      implicit
      toW: Witness.Aux[T]
  ): ^[LeafShape.Eye ><^ Const.Literal[T]] = {

    ^(Shape >|< Arity(toW))
  }

  implicit def asLeaf[T <: LeafShape](v: Aux[T]): LeafOps[T] = LeafOps(v.shape)

  case class ^[SELF <: Shape](shape: SELF) extends ShapeAPI {

    final type _Shape = SELF
  }

  // TODO: only support LeafShape, remove
//  object Vector {
//
//    type Aux[T <: Axis] = ^[➊ >< T]
//  }
//  type Vector = Vector.Aux[_]
//
//  object Matrix {
//
//    type Aux[T1 <: Axis, T2 <: Axis] = ^[➊ >< T1 >< T2]
//  }
//  type Matrix = Matrix.Aux[_ <: Axis, _ <: Axis]
}
