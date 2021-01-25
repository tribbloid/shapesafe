package com.tribbloids.shapesafe.m.shape

import com.tribbloids.shapesafe.m.arity.Expression
import com.tribbloids.shapesafe.m.axis.Axis
import com.tribbloids.shapesafe.m.axis.Axis.{->>, :<<-}
import com.tribbloids.shapesafe.m.shape.op.ShapeOps
import com.tribbloids.shapesafe.m.tuple.{StaticTuples, TupleSystem}
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

  type Record <: HList
  def record: Record

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
      prove: Shape.FromRecord.==>[ZZ, O]
  ): O = {

    val zipped: ZZ = dimensions.static.zipWithKeys(newNames.static)
    prove(zipped)
  }

  object Axes {

    def get(index: Nat)(implicit at: At[Static, index.N]): at.Out = {

//      record.reverse TODO: use it later

      static.apply(index)(at)
    }

    def get(name: Witness.Lt[String])(implicit selector: Selector[Record, name.T]): selector.Out = {

      import shapeless.record._

      record.apply(name)(selector)
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

object Shape extends TupleSystem {

  final type UpperBound = Axis

  object BackBone extends StaticTuples[UpperBound]

  final type Impl = Shape

  // Cartesian product doesn't have eye but whatever
  object Eye extends BackBone.EyeLike with Impl {

    type Record = HNil
    override def record: HNil = HNil

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
    type Record = Field :: tail.Record
    def record: Record = head.asField :: tail.record

    final override type _Names = Names.><[tail._Names, head.Name]
    final override val names = tail.names >< head.nameSingleton

    final override type _Dimensions = Dimensions.><[tail._Dimensions, head.Dimension]
    final override val dimensions = tail.dimensions.><(head.dimension)
  }

  object FromRecord extends ToEye {

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

//  object FromEinSumRecord extends FromEyeLike {
//
//    implicit def recursive[
//        TAIL <: HList,
//        PREV <: Impl,
//        D_EXISTING <: Expression,
//        N <: String,
//        D <: Expression
//    ](
//        implicit
//        forTail: TAIL ==> PREV,
//        singleton: Witness.Aux[N]
////        selector: Selector[PREV#Record, N] { type Out = D_EXISTING }
////        proveEquals: AssertEqual[D, D_EXISTING] ~~> Proof
//    ): (FieldType[N, D] :: TAIL) ==> (PREV >< (D :<<- N)) = {
//
//      { v =>
//        val prev = forTail(v.tail)
//
//        val vHead = v.head: D
//        val head = vHead :<<- singleton
//
//        prev >< head
//      }
//    }
//  }

  implicit def consAlways[TAIL <: Impl, HEAD <: UpperBound] = {
    new (TAIL Cons HEAD) {

      override type Out = TAIL >< HEAD

      override def apply(tail: TAIL, head: HEAD) = new ><(tail, head)
    }
  }

  implicit def ops[SELF <: Shape: TypeTag](self: SELF): ShapeOps[SELF] = {

    new ShapeOps(self)
  }

  implicit def toEyeOps(v: this.type): ShapeOps[Eye] = ops[Eye](Eye)
}
