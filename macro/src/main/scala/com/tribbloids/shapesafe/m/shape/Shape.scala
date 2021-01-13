package com.tribbloids.shapesafe.m.shape

import com.tribbloids.shapesafe.m.arity.Expression
import com.tribbloids.shapesafe.m.axis.Axis.:<<-
import com.tribbloids.shapesafe.m.axis.{Axis, NameUB}
import com.tribbloids.shapesafe.m.shape.OfShape.~~>
import com.tribbloids.shapesafe.m.shape.nullary.OfStatic
import shapeless.ops.hlist.ZipWithKeys
import shapeless.{::, HList, HNil}

/**
  * a thin wrapper of HList that has all proofs of constraints included
  * this saves compiler burden and reduces error
  */
trait Shape extends ShapeLike {
  // TODO: merged into TupleSystem

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
      NN <: Names.Impl,
      ZZ <: HList,
      O <: Shape
  ](newNames: NN)(
      implicit
      zipping: ZipWithKeys.Aux[newNames.Static, dimensions.Static, ZZ],
      prove: ZZ ~~> OfStatic[O]
  ): O = {

    val zipped: ZZ = dimensions.static.zipWithKeys(newNames.static)
    prove(zipped).out
  }
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

  implicit class ShapeOps[SELF <: Shape](self: SELF) {

    def cross[
        V <: Expression,
        N <: NameUB
    ](
        dim: V :<<- N
    ) = {

      ><(dim)
    }

    // DON'T Refactor! `|` has the lowest operator priority
    def ><[
        V <: Expression,
        N <: NameUB
    ](
        dim: V :<<- N
    ): ><[SELF, V :<<- N] = new Shape.><(self, dim)
  }

  def ><[
      V <: Expression,
      N <: NameUB
  ](
      dim: V :<<- N
  ) = Eye cross dim
}
