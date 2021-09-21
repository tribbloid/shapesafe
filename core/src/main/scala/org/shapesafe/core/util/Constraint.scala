package org.shapesafe.core.util

import shapeless.HList
import shapeless.ops.hlist

trait Constraint {}

object Constraint {

  import org.shapesafe.graph.commons.util.reflect.ScalaReflection._

  // TODO: why reinventing the wheel? shapeless LUBConstraint is the same
  case class ElementOfType[Data <: HList, Element: TypeTag]() extends Constraint {

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
        implicit
        assuming: hlist.ToArray[Data, Element]
    ): ElementOfType[Data, Element] =
      ElementOfType[Data, Element]()
  }

}
