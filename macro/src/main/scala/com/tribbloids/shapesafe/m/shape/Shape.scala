package com.tribbloids.shapesafe.m.shape

import com.tribbloids.shapesafe.m.arity.Expression
import com.tribbloids.shapesafe.m.axis.Axis
import com.tribbloids.shapesafe.m.axis.Axis.:<<-
import com.tribbloids.shapesafe.m.shape.op.ShapeOps
import com.tribbloids.shapesafe.m.tuple.{CanInfix, StaticTuples, TupleSystem}
import shapeless.labelled.FieldType
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

  type Static <: HList // not 'Static' which should be ann HList of [[Axis]]
  def static: Static

  //TODO: add dynamic indices
  def list: List[Axis]

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
      prove: Shape.FromRecord[ZZ, O]
  ): O = {

    val zipped: ZZ = dimensions.static.zipWithKeys(newNames.static)
    prove(zipped)
  }

  object Axes {

    def get(index: Nat)(implicit at: At[Static, index.N]): at.Out = {

//      record.reverse TODO: use it later

      static.apply(index)(at)
    }

    def get(name: Witness.Lt[String])(implicit selector: Selector[Static, name.T]): selector.Out = {

      import shapeless.record._

      static.apply(name)(selector)
    }
  }

  object EinSumHelper extends StaticTuples[String] {

    class ImplView[TAIL <: Impl, HEAD <: String](self: TAIL >< HEAD) {

      class ImplView
    }

    implicit class Ops[SELF <: Impl](val self: SELF) {}

//    implicit def canCrossIfComply[TAIL <: Impl, HEAD <: String]: CanCross[TAIL, HEAD] = { (tail, head) =>
//      val sameIndex = tail.
//
//      new ><(tail, head)
//    }
  }
}

object Shape extends TupleSystem with CanInfix {

  final type UpperBound = Axis.FieldUB
  final type Impl = Shape

  // Cartesian product doesn't have eye but whatever
  object Eye extends Impl {

    final override type Static = HNil
    final override def static: Static = HNil

    override def list: List[Axis] = Nil

    final override type _Names = Names.Eye
    final override val names = Names.Eye

    final override type _Dimensions = Dimensions.Eye
    final override val dimensions = Dimensions.Eye
  }

  // cartesian product symbol
  class ><[
      TAIL <: Shape,
      HEAD <: Axis
  ](
      val tail: TAIL,
      val head: HEAD
  ) extends Impl {

    type Tail = TAIL

    final type Field = head.Field

    final override type Static = Field :: tail.Static
    override def static: Static = head.asField :: tail.static

    override def list: List[Axis] = tail.list ++ Seq(head)

    final override type _Names = Names.><[tail._Names, head.Name]
    final override val names = tail.names >< head.nameSingleton

    final override type _Dimensions = Dimensions.><[tail._Dimensions, head.Dimension]
    final override val dimensions = tail.dimensions >< head.dimension
  }

  // TODO: lots of boilerplate can be merged with FromStatic using Poly1Group
  trait FromRecord[-I <: HList, +O <: Impl] {

    def apply(in: I): O
  }
  object FromRecord {

    implicit def toEye: HNil FromRecord Eye = { _ =>
      Eye
    }

    implicit def recursive[
        TAIL <: HList,
        PREV <: Impl,
        N <: String,
        V <: Expression
    ](
        implicit
        forTail: TAIL FromRecord PREV,
        singleton: Witness.Aux[N]
    ): (FieldType[N, V] :: TAIL) FromRecord (PREV >< (V :<<- N)) = {

      { v =>
        val prev = forTail(v.tail)
        val vHead = v.head: V
        val head = vHead :<<- singleton

        prev >< head
      }
    }

    def apply[T <: HList, O <: Impl](v: T)(implicit ev: T FromRecord O): O = {

      ev.apply(v)
    }
  }

  implicit def ops[SELF <: Shape](self: SELF): ShapeOps[SELF] = new ShapeOps(self)

  implicit def toOps(v: this.type): ShapeOps[Eye] = ops(Eye)
}
