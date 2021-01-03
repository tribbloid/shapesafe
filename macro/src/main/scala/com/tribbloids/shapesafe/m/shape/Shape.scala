package com.tribbloids.shapesafe.m.shape

import com.tribbloids.shapesafe.m.arity.Dim.{<<-, Name}
import com.tribbloids.shapesafe.m.arity.{Dim, Expression}
import com.tribbloids.shapesafe.m.shape.nullary.OfStatic
import com.tribbloids.shapesafe.m.~~>
import shapeless.ops.hlist.ZipWithKeys
import shapeless.ops.record.{Keys, Values}
import shapeless.{::, HList, HNil}

/**
  * a thin wrapper of HList that has all proofs of constraints included
  * this saves compiler burden and reduces error
  */
trait Shape extends ShapeLike {

  import Shape._

  type Self >: this.type <: Shape

  type Static <: HList
  def static: Static

  type _NameList <: HList
  protected val namesFactory: Keys.Aux[Static, _NameList]
  final lazy val names: _NameList = namesFactory()

  type _ValueList <: HList
  protected val valuesFactory: Values.Aux[Static, _ValueList]
  final lazy val values: _ValueList = valuesFactory(static)

  //TODO: add dynamic indices

  def cross[
      V <: Expression,
      N <: Name
  ](
      dim: V <<- N
  ) = {

    |(dim)
  }

  // DON'T Refactor! `|` has the lowest operator priority
  def |[
      V <: Expression,
      N <: Name
  ](
      dim: V <<- N
  ): |[Self, V <<- N] = new Shape.|(this, dim)

  def asMap: Map[String, Expression]

//  def withNamesProto[
//      NN <: HList,
//      ZZ <: HList
//  ](nameList: NamesView[NN])(
//      implicit
//      zipper: ZipWithKeys.Aux[NN, _ValueList, ZZ]
//  ): ZZ = {
//
//    val zipped: ZZ = values.zipWithKeys(nameList.self)
//    zipped
//  }

  /**
    * assign new names
    * @param nameList
    * @tparam H
    */
  def <<-[
      NN <: HList,
      ZZ <: HList,
      O <: Shape
  ](nameList: NamesView[NN])(
      implicit
      zipper: ZipWithKeys.Aux[NN, _ValueList, ZZ],
      proof: ZZ ~~> OfStatic[O]
  ): O = {

    val zipped: ZZ = values.zipWithKeys(nameList.self)
    proof(zipped).out
  }

  //  def <<-[
//      N <: HList,
//      HOut <: HList
//  ](names: NameSet[N])(
//      implicit
//      withKeys: ZipWithKeys[N, _keys.Out] { type Out <: HOut },
//      observeHList: HOut ~~> FromHList
//  ) = {
//
//    val newHList: HOut = keys.zipWithKeys(names.self)(withKeys)
//
//    observeHList(newHList).out
//  }
}

object Shape {

  class SNil extends Shape {

    final override type Self = SNil

    final override type Static = HNil
    override def static: Static = HNil

//    override val _keys: Keys[Static] = Keys.hnilKeys
//    override val _values: Values[Static] = Values.hnilValues

//    override val keys = implicitly[Keys[Static]]

    type _NameList = HNil
    type _ValueList = HNil

    override val namesFactory = Keys.hnilKeys
    override val valuesFactory = Values.hnilValues

    override def asMap: Map[String, Expression] = Map.empty
  }

  val SNil = new SNil()

  class |[
      TAIL <: Shape,
      HEAD <: Dim
  ](
      val tail: TAIL,
      val head: HEAD
  ) extends Shape {

    final override type Self = |[TAIL, HEAD]
    type Tail = TAIL

    final type Field = head.Field

    final override type Static = Field :: tail.Static

    override def static: Static = head.asField :: tail.static

    //    override val keys = {
    //      implicit val w = head.name
    //      implicit val prev: tail.keys.type = tail.keys
    //
    //      Keys.hlistKeys(w.asInstanceOf[Witness.Aux[w.T]], prev)
    //
    ////      implicitly[Keys[Static]]
    //    }

    type _NameList = head.Name :: tail._NameList
    type _ValueList = head.Value :: tail._ValueList

    override val namesFactory = {

      Keys.hlistKeys(head.nameSingleton, tail.namesFactory)
    }
    override val valuesFactory = {

      Values.hlistValues(tail.valuesFactory)
    }

    lazy val asMap: Map[String, Expression] = {

      tail.asMap.updated(head.name, head.value)
    }
  }

  def |>[
      V <: Expression,
      N <: Name
  ](
      dim: V <<- N
  ) = SNil cross dim

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
}
