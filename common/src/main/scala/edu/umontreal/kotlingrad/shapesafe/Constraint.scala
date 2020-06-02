package edu.umontreal.kotlingrad.shapesafe

import shapeless.HList
import shapeless.ops.hlist

import scala.language.implicitConversions

trait Constraint {}

object Constraint {

  import ScalaReflection.universe
  import universe.TypeTag

  case class OfElementType[Data <: HList, Element: TypeTag]() {

    val ttg: TypeTag[Element] = universe.typeTag[Element]

  }

  object OfElementType {

    // TODO: remove, not efficient

    //    implicit def observe0[T: TypeTag]: OfMemberType[HNil, T] = OfMemberType[HNil, T]()
    //
    //    implicit def observeN[T: TypeTag, Prev <: HList](
    //        implicit assuming: OfMemberType[Prev, T]): OfMemberType[T :: Prev, T] =
    //      OfMemberType[T :: Prev, T]()

    //    implicit def observe0[Element: TypeTag]: OfElementType[HNil, Element] = OfElementType[HNil, Element]()

    implicit def observe[Data <: HList, Element: TypeTag](
        implicit assuming: hlist.ToArray[Data, Element]
    ): OfElementType[Data, Element] =
      OfElementType[Data, Element]()
  }

}
