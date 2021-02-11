package org.shapesafe.core.axis

import com.tribbloids.graph.commons.util.IDMixin
import org.shapesafe.core.arity.Arity
import shapeless.Witness
import shapeless.labelled.FieldType

import scala.language.implicitConversions

trait Axis extends IDMixin {
  //TODO:; can be a subclass of shapeless KeyTag

  import Axis._

  type Dimension <: Arity
  def dimension: Dimension

  type Name <: NameWide
  def nameSingleton: Witness.Aux[Name]

  final def name: Name = nameSingleton.value

  type AxisField <: Name ->> Axis
  final def asAxisField: AxisField = this.asInstanceOf[AxisField]

  type Field <: Name ->> Dimension
  final def asField: Field = dimension.asInstanceOf[Field]

  override protected lazy val _id: Any = (dimension, name)

  override lazy val toString = s"$dimension :<<- $name"

//  final type Field = FieldType[Name, Dimension]
//  final def asField: Field = dimension.asInstanceOf[Field]
}

object Axis {

  val emptyName = ""

  // TODO: N can be eliminated
  class :<<-[
      D <: Arity,
      N <: NameWide
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
    final type AxisField = FieldType[N, D :<<- N]
    final type Field = FieldType[N, D]

    // theoretically correct but shapeless recordOps still fumble on it
  }

  type ->>[N <: NameWide, D] = FieldType[N, D]

  def apply[
      V <: Arity
  ](
      value: V,
      name: Witness.Lt[String]
  ): :<<-[V, name.T] = {

    new :<<-(value, name)
  }

  implicit def nameless[V <: Arity](self: V) = apply(self, emptyName)

  implicit class AxisOps[SELF <: Axis](self: SELF) {}
}
