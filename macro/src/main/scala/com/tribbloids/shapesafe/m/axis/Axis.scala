package com.tribbloids.shapesafe.m.axis

import com.tribbloids.graph.commons.util.IDMixin
import com.tribbloids.shapesafe.m.arity.Expression
import shapeless.Witness
import shapeless.labelled.{FieldType, KeyTag}

import scala.language.implicitConversions

trait Axis extends IDMixin {
  //TODO:; can be a subclass of shapeless KeyTag

  type Dimension <: Expression
  def dimension: Dimension

  type Name <: String
  def nameSingleton: Witness.Aux[Name]

  final def name: Name = nameSingleton.value

  type Field <: FieldType[Name, Axis]
  final def asField: Field = this.asInstanceOf[Field]

  override protected lazy val _id: Any = (dimension, name)

  override lazy val toString = s"$dimension :<<- $name"

//  final type Field = FieldType[Name, Dimension]
//  final def asField: Field = dimension.asInstanceOf[Field]
}

object Axis {

  val emptyName = ""

  // TODO: N can be eliminated
  class :<<-[
      D <: Expression,
      N <: NameUB
  ](
      val dimension: D,
      val nameSingleton: Witness.Aux[N]
  ) extends Axis
      with KeyTag[N, D :<<- N] {

    type Name = N
    type Dimension = D

    // TODO: why this can't be simplified?
    final type Field = FieldType[N, D :<<- N]
  }

  type FieldUB = :<<-[_ <: Expression, _ <: NameUB]

  def apply[
      V <: Expression
  ](
      value: V,
      name: Witness.Lt[String]
  ): :<<-[V, name.T] = {

    new :<<-(value, name)
  }

  implicit def nameless[V <: Expression](self: V) = apply(self, emptyName)

  implicit class AxisOps[SELF <: Axis](self: SELF) {}
}
