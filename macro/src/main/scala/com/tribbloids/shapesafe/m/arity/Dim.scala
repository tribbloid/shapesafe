package com.tribbloids.shapesafe.m.arity

import shapeless.Witness
import shapeless.labelled.FieldType

import scala.language.implicitConversions

trait Dim {
  //TODO:; can be a subclass of shapeless KeyTag

  type Value <: Expression
  def value: Value

  type Name <: String

  def nameSingleton: Witness.Aux[Name]

  final def name: Name = nameSingleton.value

  final type Field = FieldType[Name, Value]
  final def asField: Field = value.asInstanceOf[Field]
}

object Dim {

  type Name = String

  val emptyName = ""

  // TODO: N can be eliminated
  case class :<<-[
      V <: Expression,
      N <: Name
  ](
      value: V,
      nameSingleton: Witness.Aux[N]
  ) extends Dim {

    type Name = N

    type Value = V
  }

  type UB = FieldType[_ <: Name, Expression]

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
