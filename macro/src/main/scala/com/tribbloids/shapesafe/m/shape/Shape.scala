package com.tribbloids.shapesafe.m.shape

import com.tribbloids.shapesafe.m.arity.Expression
import com.tribbloids.shapesafe.m.axis.Axis
import com.tribbloids.shapesafe.m.axis.Axis.{->>, :<<-}
import com.tribbloids.shapesafe.m.shape.EinSumOperand.><
import com.tribbloids.shapesafe.m.shape.op.ShapeOps
import com.tribbloids.shapesafe.m.tuple.{CanFromStatic, StaticTuples, TupleSystem}
import com.tribbloids.shapesafe.m.util.TypeTag
import shapeless.ops.hlist.{At, ZipWithKeys}
import shapeless.ops.record.Selector
import shapeless.{::, HList, HNil, Nat, Witness}

import scala.language.implicitConversions

/**
  * a thin wrapper of HList that has all proofs of constraints included
  * this saves compiler burden and reduces error
  */
trait Shape extends Shape.BackBone.Impl {

  type Lookup <: HList // name: String -> axis: Axis
  def lookup: Lookup

  type Name_Dimension <: HList // name: String -> dim: arity.Expression
  def name_Dimension: Name_Dimension

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
      prove: Shape.FromName_Dimension.==>[ZZ, O]
  ): O = {

    val zipped: ZZ = dimensions.static.zipWithKeys(newNames.static)
    prove(zipped)
  }

  object Axes {

    def get(index: Nat)(implicit at: At[Static, index.N]): at.Out = {

//      record.reverse TODO: use it later

      static.apply(index)(at)
    }

    def get(name: Witness.Lt[String])(implicit selector: Selector[Lookup, name.T]): selector.Out = {

      import shapeless.record._

      lookup.apply(name)(selector)
    }
  }

//  def asEinSumOperand[
//      ZZ <: HList,
//      O <: Shape
//  ](newNames: Names.Impl)(
//      implicit
//      zipping: ZipWithKeys.Aux[newNames.Static, dimensions.Static, ZZ],
//      prove: Shape.FromEinSumRecord.==>[ZZ, O]
//  ): O = {
//
//    val zipped: ZZ = dimensions.static.zipWithKeys(newNames.static)
//    prove(zipped)
//  }
//
//  object EinSumHelper extends StaticTuples[String] {
//
//    class ImplView[TAIL <: Impl, HEAD <: String](self: TAIL >< HEAD) {
//
//      class ImplView
//    }
//
//    implicit class Ops[SELF <: Impl](val self: SELF) {}
//
////    implicit def canCrossIfComply[TAIL <: Impl, HEAD <: String]: CanCross[TAIL, HEAD] = { (tail, head) =>
////      val sameIndex = tail.
////
////      new ><(tail, head)
////    }
//  }
}

object Shape extends TupleSystem with CanFromStatic {

  final type UpperBound = Axis

  object BackBone extends StaticTuples[UpperBound]

  final type Impl = Shape

  // Cartesian product doesn't have eye but whatever
  object eye extends BackBone.EyeLike with Impl {

    type Lookup = HNil
    override def lookup: HNil = HNil

    type Name_Dimension = HNil
    override def name_Dimension: Name_Dimension = HNil

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
  ) extends BackBone.><[TAIL, HEAD](tail, head)
      with Impl {

    final type Field = head.Field

    type Lookup = Field :: tail.Lookup
    lazy val lookup: Lookup = head.asField :: tail.lookup

    override type Name_Dimension = head.Dimension :: tail.Name_Dimension
    override lazy val name_Dimension: Name_Dimension = head.dimension :: tail.name_Dimension

    final override type _Names = Names.><[tail._Names, head.Name]
    final override val names = tail.names >< head.nameSingleton

    final override type _Dimensions = Dimensions.><[tail._Dimensions, head.Dimension]
    final override val dimensions = new Dimensions.><(tail.dimensions, head.dimension)

  }

  object FromName_Dimension extends HListConverter {

    implicit def recursive[
        TAIL <: HList,
        PREV <: Impl: TypeTag,
        N <: String,
        D <: Expression
    ](
        implicit
        forTail: TAIL ==> PREV,
        singleton: Witness.Aux[N]
    ): ((N ->> D) :: TAIL) ==> (PREV >< (D :<<- N)) = {

      { v =>
        val prev = forTail(v.tail)
        val vHead = v.head: D
        val head = vHead :<<- singleton

        prev.><(head)
      }
    }
  }

  implicit def consAlways[TAIL <: Impl, HEAD <: UpperBound]: Cons.FromFn[TAIL, HEAD, TAIL >< HEAD] = {

    Cons[TAIL, HEAD].build { (tail, head) =>
      new ><(tail, head)
    }
  }

  implicit def ops[SELF <: Shape: TypeTag](self: SELF): ShapeOps[SELF] = {

    new ShapeOps(self)
  }

  implicit def toEyeOps(v: this.type): ShapeOps[Eye] = ops[Eye](Eye)
}
