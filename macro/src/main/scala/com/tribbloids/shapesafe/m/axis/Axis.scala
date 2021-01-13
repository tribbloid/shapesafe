package com.tribbloids.shapesafe.m.axis

import com.tribbloids.shapesafe.m.arity.Expression
import shapeless.Witness
import shapeless.labelled.FieldType

import scala.language.implicitConversions

trait Axis {
  //TODO:; can be a subclass of shapeless KeyTag

  type Dimension <: Expression
  def dimension: Dimension

  type Name <: String
  def nameSingleton: Witness.Aux[Name]

  final def name: Name = nameSingleton.value

  final type Field = FieldType[Name, Dimension]
  final def asField: Field = dimension.asInstanceOf[Field]
}

object Axis {

  val emptyName = ""

  // TODO: N can be eliminated
  case class :<<-[
      V <: Expression,
      N <: NameUB
  ](
      dimension: V,
      nameSingleton: Witness.Aux[N]
  ) extends Axis {

    type Name = N
    type Dimension = V
  }

  type FieldUB = FieldType[_ <: NameUB, Expression]

  def apply[
      V <: Expression
  ](
      value: V,
      name: Witness.Lt[String]
  ): :<<-[V, name.T] = {

    new :<<-(value, name)
  }

  implicit def nameless[V <: Expression](self: V) = apply(self, emptyName)
}
