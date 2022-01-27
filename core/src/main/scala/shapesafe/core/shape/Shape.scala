package shapesafe.core.shape

import shapeless.ops.hlist.Reverse
import shapeless.{HList, Nat, SingletonProductArgs, Witness}
import shapesafe.core.arity.Utils.NatAsOp
import shapesafe.core.arity.{Arity, ConstArity}
import shapesafe.core.shape.ProveShape.|-
import shapesafe.core.shape.StaticShape.{><^, Eye}
import shapesafe.core.shape.args.{ApplyLiterals, ApplyNats}
import shapesafe.core.shape.binary.OuterProduct
import shapesafe.core.shape.ops.{EinSumOps, MatrixOps, StaticOpsView, VectorOps}
import shapesafe.core.shape.unary._
import shapesafe.core.{Ops, XInt}

import scala.language.implicitConversions

trait Shape extends VectorOps with MatrixOps {

  import Shape._

  final override def toString: String = shapeType.toString

  final def verify[
      O <: ShapeType
  ](
      implicit
      prove: _ShapeType |- O
  ): ^[O] = prove.instanceFor(shapeType).^

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
      prove: _ShapeType |- O
  ): ^[O] = verify(prove)

  def peek(
      implicit
      reporter: ShapeReporters.PeekShape.Case[_ShapeType]
  ): this.type = this

  def interrupt(
      implicit
      reporter: ShapeReporters.InterruptShape.Case[_ShapeType]
  ): this.type = this

  def reason[
      O <: LeafShape
  ](
      implicit
      reporter: ShapeReporters.PeekShape.Case[_ShapeType],
      prove: _ShapeType |- O
  ): ^[O] = eval(prove)

  /**
    * assign new names
    */
  object namedBy {

    def apply[N <: Names](newNames: N): ^[ZipWithNames[_ShapeType, N]] = {

      new ZipWithNames[_ShapeType, N](shapeType.^, newNames).^
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
    ): ^[ZipWithNames[_ShapeType, lemma.Out]] = {

      val out = lemma.apply(reverse(v))

      namedBy.apply(out)
    }
  }

  lazy val :<<=* : named.type = named

  object >< {

    def apply(
        that: Shape
    ): ^[OuterProduct[_ShapeType, that._ShapeType]] = {

      OuterProduct(shapeType, that.shapeType).^
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

  def einSum: EinSumOps[_ShapeType] = EinSumOps(shapeType)

  def contract[N <: Names](names: N): ^[Select[CheckEinSum[_ShapeType], N]] = {

    einSum.-->(names)
  }

  object select1 {

    def apply[T <: Index](v: T): ^[GetSubscript[_ShapeType, T]] = {
      GetSubscript(shapeType, v).^
    }

    def apply(i: Nat)(
        implicit
        asOp: NatAsOp[i.N]
    ): ^[GetSubscript[_ShapeType, Index.I_th[i.N, asOp.OutInt]]] = {

      apply(Index.I_th(i))
    }

    def apply(w: Witness.Lt[String]): ^[GetSubscript[_ShapeType, Index.Name[w.T]]] = {

      apply(Index.Name(w))
    }
  }

  def selectBy[II <: IndicesMagnet](indices: II): ^[Select[RequireDistinct[_ShapeType], II]] = {

    val distinct = RequireDistinct(shapeType)
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
    ): ^[Select[RequireDistinct[_ShapeType], lemma.Out]] = {

      val out = lemma.apply(reverse(v))

      selectBy(out)
    }
  }

  def reduceByName(
      infix: Ops.Infix
  ) = infix.reduceByName(this)

  def flattenByName = reduceByName(Ops.:*)

  def applyPerDim(
      infix: Ops.Infix,
      that: Shape
  ) = infix.applyPerDim(this, that)

  def requireEqual(
      that: Shape
  ): ^[Ops.==!._Op2PerDim.On[_ShapeType, that._ShapeType]] = applyPerDim(Ops.==!, that)
}

object Shape extends Shape with ApplyLiterals.ToShape {

  object Nats extends ApplyNats.ToShape {}

  object Literals extends ApplyLiterals.ToShape {}

  type Aux[T] = Shape { type _ShapeType = T }

  implicit def unbox[S <: ShapeType](v: Aux[S]): S = v.shapeType

  implicit def fromXInt[T <: XInt](v: T)(
      implicit
      toW: Witness.Aux[T]
  ): ^[StaticShape.Eye ><^ ConstArity.Literal[T]] = {

    ^(Shape and Arity(toW))
  }

  implicit def asStatic[T <: StaticShape](v: Aux[T]): StaticOpsView[T] = StaticOpsView(v.shapeType)

  case class ^[SELF <: ShapeType](shapeType: SELF) extends Shape {

    final type _ShapeType = SELF
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

  val Unchecked: Shape.^[shapesafe.core.shape.Unchecked.type] = shapesafe.core.shape.Unchecked.^

  def box[T <: ShapeType](self: T): Shape.^[T] = Shape.^(self)

  override type _ShapeType = StaticShape.Eye
  override def shapeType: Eye = StaticShape.Eye
}
