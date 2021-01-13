package com.tribbloids.shapesafe.m.shape

import com.tribbloids.shapesafe.m.arity.Dim.{:<<-, Name}
import com.tribbloids.shapesafe.m.arity.{Dim, Expression}
import com.tribbloids.shapesafe.m.shape.OfShape.~~>
import com.tribbloids.shapesafe.m.shape.nullary.OfStatic
import shapeless.ops.hlist.ZipWithKeys
import shapeless.ops.record.{Keys, Values}
import shapeless.{::, HList, HNil}

/**
  * a thin wrapper of HList that has all proofs of constraints included
  * this saves compiler burden and reduces error
  */
trait Shape extends ShapeLike {

  type Static <: HList
  def static: Static

  type _NameList <: HList
  protected val namesFactory: Keys.Aux[Static, _NameList]
  final lazy val names: _NameList = namesFactory()

  type _ValueList <: HList
  protected val valuesFactory: Values.Aux[Static, _ValueList]
  final lazy val values: _ValueList = valuesFactory(static)

  //TODO: add dynamic indices

  def list: List[Dim]

  /**
    * assign new names
    * @param names
    * @tparam H
    */
  def |<<-[
      NN <: Names.Impl,
      ZZ <: HList,
      O <: Shape
  ](names: NN)(
      implicit
      zipping: ZipWithKeys.Aux[names.Static, _ValueList, ZZ],
      prove: ZZ ~~> OfStatic[O]
  ): O = {

    val zipped: ZZ = values.zipWithKeys(names.static)
    prove(zipped).out
  }
}

object Shape {

  // Cartesian product doesn't have eye but whatever
  sealed class Eye extends Shape {

    final override type Static = HNil
    override def static: Static = HNil

    type _NameList = HNil
    type _ValueList = HNil

    override val namesFactory = Keys.hnilKeys
    override val valuesFactory = Values.hnilValues

    override def list: List[Dim] = Nil
  }

  val Eye = new Eye()

  // cartesian product symbol
  class ><[
      TAIL <: Shape,
      HEAD <: Dim
  ](
      val tail: TAIL,
      val head: HEAD
  ) extends Shape {

    type Tail = TAIL

    final type Field = head.Field

    final override type Static = Field :: tail.Static
    override def static: Static = head.asField :: tail.static

    type _NameList = head.Name :: tail._NameList
    type _ValueList = head.Value :: tail._ValueList

    override val namesFactory = {

      Keys.hlistKeys(head.nameSingleton, tail.namesFactory)
    }
    override val valuesFactory = {

      Values.hlistValues(tail.valuesFactory)
    }

    override def list: List[Dim] = tail.list ++ Seq(head)
  }

  def fromHList[
      H <: HList
  ](list: H): Unit = {}

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
        N <: Name
    ](
        dim: V :<<- N
    ) = {

      ><(dim)
    }

    // DON'T Refactor! `|` has the lowest operator priority
    def ><[
        V <: Expression,
        N <: Name
    ](
        dim: V :<<- N
    ): ><[SELF, V :<<- N] = new Shape.><(self, dim)
  }

  def ><[
      V <: Expression,
      N <: Name
  ](
      dim: V :<<- N
  ) = Eye cross dim
}
