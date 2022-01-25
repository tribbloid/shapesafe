package shapesafe.core.shape

import shapesafe.core.{Ops, XInt}
import shapesafe.core.arity.Utils.NatAsOp
import shapesafe.core.arity.{Arity, ConstArity}
import shapesafe.core.shape.ProveShape.|-
import shapesafe.core.shape.StaticShape.><^
import shapesafe.core.shape.binary.OuterProduct
import shapesafe.core.shape.ops.{EinSumOps, MatrixOps, StaticOps, VectorOps}
import shapesafe.core.shape.unary._
import shapeless.ops.hlist.Reverse
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
  ): ^[O] = prove.instanceFor(shape).^

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
  object namedBy {

    def apply[N <: Names](newNames: N): ^[ZipWithNames[_Shape, N]] = {

      new ZipWithNames[_Shape, N](shape.^, newNames).^
    }
  }

  lazy val :<<= : namedBy.type = namedBy

  // no need for Names constructor
  object named extends SingletonProductArgs {

    def applyProduct[H1 <: HList, H2 <: HList](
        v: H1
    )(
        implicit
        reverse: Reverse.Aux[H1, H2],
        lemma: Names.FromLiterals.Case[H2]
    ): ^[ZipWithNames[_Shape, lemma.Out]] = {

      val out = lemma.apply(reverse(v))

      namedBy.apply(out)
    }
  }

  lazy val :<<=* : named.type = named

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

  def contract[N <: Names](names: N): ^[Select[CheckEinSum[_Shape], N]] = {

    einSum.-->(names)
  }

  object select1 {

    def apply[T <: Index](v: T): ^[GetSubscript[_Shape, T]] = {
      GetSubscript(shape, v).^
    }

    def apply(i: Nat)(
        implicit
        asOp: NatAsOp[i.N]
    ): ^[GetSubscript[_Shape, Index.I_th[i.N, asOp.OutInt]]] = {

      apply(Index.I_th(i))
    }

    def apply(w: Witness.Lt[String]): ^[GetSubscript[_Shape, Index.Name[w.T]]] = {

      apply(Index.Name(w))
    }
  }

  def selectBy[II <: IndicesMagnet](indices: II): ^[Select[RequireDistinct[_Shape], II]] = {

    val distinct = RequireDistinct(shape)
    val result = Select(distinct, indices)

    result.^
  }

  object select extends SingletonProductArgs {

    def applyProduct[H1 <: HList, H2 <: HList](
        v: H1
    )(
        implicit
        reverse: Reverse.Aux[H1, H2],
        lemma: Names.FromLiterals.Case[H2]
    ): ^[Select[RequireDistinct[_Shape], lemma.Out]] = {

      val out = lemma.apply(reverse(v))

      selectBy(out)
    }
  }

  def reduceByName(
      infix: Ops.Infix
  ) = infix.reduceByName(this)

  def flattenByName = reduceByName(Ops.:*)

  def foreachAxis(
      infix: Ops.Infix,
      that: ShapeAPI
  ) = infix.foreachAxis(this, that)

  def requireEqual(
      that: ShapeAPI
  ): ^[Ops.==!._ForEachAxis.On[_Shape, that._Shape]] = foreachAxis(Ops.==!, that)
}

object ShapeAPI {

  type Aux[T] = ShapeAPI { type _Shape = T }

  implicit def unbox[S <: Shape](v: Aux[S]): S = v.shape

  implicit def fromXInt[T <: XInt](v: T)(
      implicit
      toW: Witness.Aux[T]
  ): ^[StaticShape.Eye ><^ ConstArity.Literal[T]] = {

    ^(Shape append Arity(toW))
  }

  implicit def asStatic[T <: StaticShape](v: Aux[T]): StaticOps[T] = StaticOps(v.shape)

  case class ^[SELF <: Shape](shape: SELF) extends ShapeAPI {

    final type _Shape = SELF
  }

  // TODO: only support LeafShape, remove
//  object Vector {
//
//    type Aux[T <: Axis] = ^[∅ >< T]
//  }
//  type Vector = Vector.Aux[_]
//
//  object Matrix {
//
//    type Aux[T1 <: Axis, T2 <: Axis] = ^[∅ >< T1 >< T2]
//  }
//  type Matrix = Matrix.Aux[_ <: Axis, _ <: Axis]
}
