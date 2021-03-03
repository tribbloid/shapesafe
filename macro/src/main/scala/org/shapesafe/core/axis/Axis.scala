package org.shapesafe.core.axis

import com.tribbloids.graph.commons.util.IDMixin
import org.shapesafe.core.arity.Arity
import org.shapesafe.core.shape.LeafShape.><
import org.shapesafe.core.shape.{LeafShape, ProveShape, Shape}
import shapeless.Witness
import shapeless.labelled.FieldType

import scala.language.implicitConversions

trait Axis extends IDMixin {
  //TODO:; can be a subclass of shapeless KeyTag

  type Dimension <: Arity
  def dimension: Dimension

  type Name <: String
  def nameSingleton: Witness.Aux[Name]

  final def name: Name = nameSingleton.value

  final type Field = FieldType[Name, Dimension]
  final def asField: Field = dimension.asInstanceOf[Field]

  override protected lazy val _id: Any = (dimension, name)

//  final type Field = FieldType[Name, Dimension]
//  final def asField: Field = dimension.asInstanceOf[Field]
}

object Axis {

  import ProveShape._

  type ->>[N <: String, D] = FieldType[N, D]
  type UB_->> = (_ <: String) ->> Arity

  // TODO: N can be eliminated
  class :<<-[
      D <: Arity,
      N <: String
  ](
      val dimension: D,
      val nameSingleton: Witness.Aux[N]
  ) extends Axis
//      with KeyTag[N, D :<<- N]
      // TODO: remove? FieldType[] has some black magic written in macro
      {

    type Name = N
    type Dimension = D

    override lazy val toString = s"$dimension :<<- $name"
  }

  val emptyName = ""
  val unknownName = "??"

  //  val emptyW = Witness(emptyName) // for singleton String, can cast from emptyName automatically
  trait Nameless extends Axis {

    override type Name = emptyName.type
    override def nameSingleton = Witness(emptyName)
  }

  def apply[
      V <: Arity
  ](
      value: V,
      name: Witness.Lt[String]
  ): :<<-[V, name.T] = {

    new :<<-(value, name)
  }

  // TODO: alternatively: make Axis extending LeafShape directly?
  implicit def axisAsLeaf[A <: Axis]: A =>> (LeafShape.Eye >< A) = {

    forAll[A].=>> { axis =>
      Shape >|< axis
    }
  }
}
