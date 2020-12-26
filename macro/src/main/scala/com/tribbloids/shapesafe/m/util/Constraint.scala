package com.tribbloids.shapesafe.m.util

import shapeless.HList
import shapeless.ops.hlist

import scala.language.implicitConversions

trait Constraint {}

object Constraint {

  import com.tribbloids.graph.commons.util.ScalaReflection.universe

  case class ElementOfType[Data <: HList, Element: TypeTag]() {

    val ttg: TypeTag[Element] = universe.typeTag[Element]

  }

  object ElementOfType {

    // TODO: remove, not efficient

    //    implicit def observe0[T: TypeTag]: OfMemberType[HNil, T] = OfMemberType[HNil, T]()
    //
    //    implicit def observeN[T: TypeTag, Prev <: HList](
    //        implicit assuming: OfMemberType[Prev, T]): OfMemberType[T :: Prev, T] =
    //      OfMemberType[T :: Prev, T]()

    //    implicit def observe0[Element: TypeTag]: OfElementType[HNil, Element] = OfElementType[HNil, Element]()

    implicit def observe[Data <: HList, Element: TypeTag](
        implicit assuming: hlist.ToArray[Data, Element]
    ): ElementOfType[Data, Element] =
      ElementOfType[Data, Element]()
  }

}
