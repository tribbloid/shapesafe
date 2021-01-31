package com.tribbloids.shapesafe.m.shape

import com.tribbloids.shapesafe.m.arity.Utils.NatAsOp
import com.tribbloids.shapesafe.m.arity.{Arity, Expression}
import com.tribbloids.shapesafe.m.axis.Axis
import com.tribbloids.shapesafe.m.axis.Axis.{->>, :<<-}
import com.tribbloids.shapesafe.m.shape.op.{EinSumIndexed, EinSumOps, ShapeOps}
import com.tribbloids.shapesafe.m.tuple.{CanFromStatic, StaticTuples, TupleSystem}
import shapeless.ops.hlist.{At, ZipWithKeys}
import shapeless.ops.record.Selector
import shapeless.{::, HList, HNil, Nat, NatProductArgs, Witness}

import scala.language.implicitConversions

/**
  * a thin wrapper of HList that has all proofs of constraints included
  * this saves compiler burden and reduces error
  */
trait Shape extends Shape.Proto {

  type NamedIndex <: HList // name: String -> axis: Axis
  def namedIndex: NamedIndex

  type Index <: HList // name: String -> dim: arity.Expression
  def index: Index

  type _Names <: Names.Impl
  val names: _Names

  type _Dimensions <: Dimensions.Impl
  val dimensions: _Dimensions

  /**
    * assign new names
    * @param newNames a tuple of names
    */
  def |<<-[
      ZZ <: HList,
      O <: Shape
  ](newNames: Names.Impl)(
      implicit
      zipping: ZipWithKeys.Aux[newNames.Static, dimensions.Static, ZZ],
      prove: Shape.FromIndex.=:=>[ZZ, O]
  ): O = {

    val zipped: ZZ = dimensions.static.zipWithKeys(newNames.static)
    Shape.FromIndex(zipped)
  }

  object Axes {

    def get(index: Nat)(implicit at: At[Static, index.N]): at.Out = {

//      record.reverse TODO: use it later

      static.apply(index)(at)
    }

    def get(name: Witness.Lt[String])(implicit selector: Selector[NamedIndex, name.T]): selector.Out = {

      import shapeless.record._

      namedIndex.apply(name)(selector)
    }
  }

}

object Shape extends TupleSystem with CanFromStatic with NatProductArgs {

  final type UpperBound = Axis

  object Proto extends StaticTuples[UpperBound]
  type Proto = Proto.Impl

  final type Impl = Shape

  // Cartesian product doesn't have eye but whatever
  object eye extends Proto.EyeLike with Impl {

    type NamedIndex = HNil
    override def namedIndex: HNil = HNil

    type Index = HNil
    override def index: Index = HNil

    final override type _Names = Names.Eye
    final override val names = Names.Eye

    final override type _Dimensions = Dimensions.Eye
    final override val dimensions = Dimensions.Eye
  }

  // cartesian product symbol
  class ><[
      TAIL <: Impl,
      HEAD <: UpperBound
  ](
      override val tail: TAIL,
      override val head: HEAD
  ) extends Proto.><[TAIL, HEAD](tail, head)
      with Impl {

    final type AxisField = head.AxisField
    override type NamedIndex = AxisField :: tail.NamedIndex
    lazy val namedIndex: NamedIndex = head.asAxisField :: tail.namedIndex

    final type Field = head.Field
    override type Index = head.Field :: tail.Index
    override lazy val index: Index = head.asField :: tail.index

    final override type _Names = Names.><[tail._Names, head.Name]
    final override val names = tail.names >< head.nameSingleton

    final override type _Dimensions = Dimensions.><[tail._Dimensions, head.Dimension]
    final override val dimensions = new Dimensions.><(tail.dimensions, head.dimension)

  }

  object FromIndex extends HListConverter {

    implicit def inductive[
        H_TAIL <: HList,
        TAIL <: Impl,
        N <: String, // CAUTION: cannot be reduced to w.T! Scala compiler is too dumb to figure it out
        D <: Expression
    ](
        implicit
        forTail: H_TAIL =:=> TAIL,
        w: Witness.Aux[N]
    ): ((N ->> D) :: H_TAIL) =:=> (TAIL >< (D :<<- N)) = {

      buildFrom[(N ->> D) :: H_TAIL].to { v =>
        val prev = apply(v.tail)
        val vHead = v.head: D
        val head = vHead :<<- w

        prev >< head
      }
    }
  }

  implicit def consAlways[TAIL <: Impl, HEAD <: UpperBound]: Cons.FromFn[TAIL, HEAD, TAIL >< HEAD] = {

    Cons[TAIL, HEAD].to { (tail, head) =>
      new ><(tail, head)
    }
  }

  implicit def ops[SELF <: Shape](self: SELF): ShapeOps[SELF] = {

    new ShapeOps(self)
  }

  implicit def toEyeOps(v: this.type): ShapeOps[Eye] = ops[Eye](Eye)

  implicit def einSumOps[
      S <: Shape
  ](self: S)(
      implicit
      checkThis: EinSumIndexed.FromStatic.Spec[self.Index]
  ) = {

    val indexed = EinSumIndexed.FromStatic.apply(self.index)

    EinSumOps(Seq(self))(indexed)
  }

  //TODO: doesn't work, blocked by https://github.com/milessabin/shapeless/issues/1072
  object FromLiterals extends HListConverter {

    implicit def inductive[
        H_TAIL <: HList,
        TAIL <: Impl,
        HEAD <: Int
    ](
        implicit
        forTail: H_TAIL =:=> TAIL,
        w: Witness.Aux[HEAD]
    ): (HEAD :: H_TAIL) =:=> ><[TAIL, Arity.FromLiteral[w.T] :<<- Axis.emptyName.type] = {

      buildFrom[w.T :: H_TAIL].to { v =>
        val prev = apply(v.tail)
        val head = Arity.FromLiteral(w) :<<- Axis.emptyName

        prev >< head
      }
    }
  }

//  def applyProduct[T <: HList](v: T)(implicit ev: FromLiterals.Case[T]): ev.Out = {
//    ev.apply(v)
//  }

  object FromNats extends HListConverter {

    implicit def inductive[
        H_TAIL <: HList,
        TAIL <: Impl,
        HEAD <: Nat
    ](
        implicit
        forTail: H_TAIL =:=> TAIL,
        ev: NatAsOp[HEAD]
    ): (HEAD :: H_TAIL) =:=> ><[TAIL, Arity.FromOp[NatAsOp[HEAD]] :<<- Axis.emptyName.type] = {

      buildFrom[(HEAD :: H_TAIL)].to { v =>
        val prev = apply(v.tail)
        val head = Arity.FromNat(v.head) :<<- Axis.emptyName

        prev >< head
      }
    }
  }

  // TODO: should this be reversed?
  //  HList :: operator is right to left
  //  our >< operator is left to right
  def applyNatProduct[T <: HList](v: T)(implicit ev: FromNats.Spec[T]): ev.Out = {
    FromNats.apply(v)
  }
}
