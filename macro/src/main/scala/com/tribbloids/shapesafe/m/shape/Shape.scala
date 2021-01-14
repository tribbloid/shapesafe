package com.tribbloids.shapesafe.m.shape

import com.tribbloids.shapesafe.m.axis.Axis
import com.tribbloids.shapesafe.m.shape.OfShape.~~>
import com.tribbloids.shapesafe.m.shape.nullary.OfStatic
import com.tribbloids.shapesafe.m.shape.op.ShapeOps
import shapeless.ops.hlist.{At, ZipWithKeys}
import shapeless.ops.record.Selector
import shapeless.{::, HList, HNil, Nat, Witness}

import scala.language.implicitConversions

/**
  * a thin wrapper of HList that has all proofs of constraints included
  * this saves compiler burden and reduces error
  */
trait Shape {
  // TODO: merged into TupleSystem?

  type _Record <: HList // not 'Static' which should be ann HList of [[Axis]]
  def record: _Record

  //TODO: add dynamic indices
  def list: List[Axis]

  type _Names <: Names.Impl
  val names: _Names

  type _Dimensions <: Dimensions.Impl
  val dimensions: _Dimensions

  /**
    * assign new names
    * @param newNames
    * @tparam H
    */
  def |<<-[
      ZZ <: HList,
      O <: Shape
  ](newNames: Names.Impl)(
      implicit
      zipping: ZipWithKeys.Aux[newNames.Static, dimensions.Static, ZZ],
      prove: ZZ ~~> OfStatic[O]
  ): O = {

    val zipped: ZZ = dimensions.static.zipWithKeys(newNames.static)
    prove(zipped).out
  }

  object Axes {

    def get(index: Nat)(implicit at: At[_Record, index.N]): at.Out = {

//      record.reverse TODO: use it later

      record.apply(index)(at)
    }

    def get(name: Witness.Lt[String])(implicit selector: Selector[_Record, name.T]): selector.Out = {

      import shapeless.record._

      record.apply(name)(selector)
    }
  }

  //  object EinSum extends TupleSystem[String] {
//
//    class Ops[SELF <: Impl](self: SELF) {}
//    override def getOps[SELF <: Impl](self: SELF): Ops[SELF] = new Ops(self)
//  }
}

object Shape {

  // Cartesian product doesn't have eye but whatever
  sealed class Eye extends Shape {

    final override type _Record = HNil
    final override def record: _Record = HNil

    override def list: List[Axis] = Nil

    final override type _Names = Names.Eye
    final override val names = Names.Eye

    final override type _Dimensions = Dimensions.Eye
    final override val dimensions = Dimensions.Eye
  }
  val Eye = new Eye()

  // cartesian product symbol
  class ><[
      TAIL <: Shape,
      HEAD <: Axis
  ](
      val tail: TAIL,
      val head: HEAD
  ) extends Shape {

    type Tail = TAIL

    final type Field = head.Field

    final override type _Record = Field :: tail._Record
    override def record: _Record = head.asField :: tail.record

    override def list: List[Axis] = tail.list ++ Seq(head)

    final override type _Names = Names.><[tail._Names, head.Name]
    final override val names = tail.names >< head.nameSingleton

    final override type _Dimensions = Dimensions.><[tail._Dimensions, head.Dimension]
    final override val dimensions = tail.dimensions >< head.dimension
  }

  def ofStatic[
      H <: HList,
      O <: Shape
  ](
      record: H
  )(
      implicit lemma: H ~~> OfStatic[O]
  ): O = {

    lemma(record).out
  }

  implicit def ops[SELF <: Shape](self: SELF): ShapeOps[SELF] = new ShapeOps(self)

  implicit def toOps(v: this.type): ShapeOps[Eye] = ops(Eye)
}
