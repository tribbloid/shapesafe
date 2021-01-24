package com.tribbloids.shapesafe.m.axis

import com.tribbloids.graph.commons.util.IDMixin
import com.tribbloids.shapesafe.m.arity.Expression
import shapeless.Witness
import shapeless.labelled.FieldType

import scala.language.implicitConversions

trait Axis extends IDMixin {

  import Axis._

  //TODO:; can be a subclass of shapeless KeyTag

  type Dimension <: Expression
  def dimension: Dimension

  type Name <: NameUB
  def nameSingleton: Witness.Aux[Name]

  final def name: Name = nameSingleton.value

  type Field <: AxisField[Dimension, Name]
  def asField: Field

  override protected lazy val _id: Any = (dimension, name)

  override lazy val toString = s"$dimension :<<- $name"
}

object Axis {

  val emptyName = ""

  type Aux[D <: Expression] = Axis {
    type Dimension = D
  }

  type AxisField[D <: Expression, N <: NameUB] = FieldType[N, Aux[D]]

  type :<<-[D <: Expression, N <: NameUB] = FieldType[N, Named[D, N]]

  // TODO: N can be eliminated?
  class Named[
      D <: Expression,
      N <: NameUB
  ](
      val dimension: D,
      val nameSingleton: Witness.Aux[N]
  ) extends Axis
//      with KeyTag[N, D :<<- N]
      // TODO: remove? FieldType[] has some black magic written in macro
      {

    type Name = N
    type Dimension = D

    // TODO: why this can't be simplified?
    final type Field = D :<<- N
    override def asField: Field = this.asInstanceOf[Field]

    // theoretically correct but shapeless recordOps still fumble on it
  }

  def apply[
      D <: Expression
  ](
      value: D,
      name: Witness.Lt[String]
  ): D :<<- name.T = {

    new Named[D, name.T](value, name).asField
  }

  implicit def nameless[V <: Expression](self: V): :<<-[V, emptyName.type] =
    apply(self, emptyName)

  implicit class AxisOps[SELF <: Axis](self: SELF) {}
}
