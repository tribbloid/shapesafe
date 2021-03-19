package org.shapesafe.core.axis

import com.tribbloids.graph.commons.util.IDMixin
import org.shapesafe.core.arity.{Arity, ArityAPI, HasArity}
import org.shapesafe.core.axis.Axis.:<<-
import org.shapesafe.core.shape.LeafShape.><
import org.shapesafe.core.shape.{LeafShape, ProveShape, Shape}
import shapeless.Witness
import shapeless.labelled.FieldType

import scala.language.implicitConversions

trait Axis extends HasArity with IDMixin {
  //TODO:; can be a subclass of shapeless KeyTag

  type Name <: String
  def nameSingleton: Witness.Aux[Name]

  final def name: Name = nameSingleton.value

  final type Field = FieldType[Name, Internal]
  final def asField: Field = internal.asInstanceOf[Field]

//  final def namedField(name: Witness.Lt[String]): FieldType[name.T, ArityCore] = {
//    named(name).asField
//  }

  override protected lazy val _id: Any = (internal, name)

//  final type Field = FieldType[Name, Dimension]
//  final def asField: Field = dimension.asInstanceOf[Field]

  def nameless: ArityAPI.^[Internal] = internal.^

  def namedT[S <: String](
      implicit
      name: Witness.Aux[S]
  ): Internal :<<- S = Axis(nameless, name)

  def named(name: Witness.Lt[String]): Internal :<<- name.T = {

    namedT(name)
  }

  def :<<-(name: Witness.Lt[String]): Internal :<<- name.T = namedT(name)
}

object Axis {

  import ProveShape.Factory._
  import ProveShape._

  type ->>[N <: String, D] = FieldType[N, D]
  type UB_->> = (_ <: String) ->> Arity

  // TODO: N can be eliminated
  class :<<-[
      A <: Arity,
      N <: String
  ](
      val internal: A,
      val nameSingleton: Witness.Aux[N]
  ) extends Axis
//      with KeyTag[N, D :<<- N]
      // TODO: remove? FieldType[] has some black magic written in macro
      {

    type Name = N
    type Internal = A

    override lazy val toString: String = {
      s"$internal :<<- $name"
    }
  }

//  def Nameless[D <: ArityCore](
//      dimension: D
//  ): D :<<- NoName = {
//    dimension :<<- NoName
//  }

  //  val emptyW = Witness(emptyName) // for singleton String, can cast from emptyName automatically
//  class Nameless[D <: Arity](
//      val dimension: D
//  ) extends Axis {
//
//    override type Name = emptyName.type
//    override def nameSingleton = Witness(emptyName)
//
//    override type Dimension = D
//
//    override lazy val toString = s"$dimension"
//  }

  def apply(
      value: ArityAPI,
      name: Witness.Lt[String]
  ): :<<-[value.Internal, name.T] = {

    new :<<-(value.internal, name)
  }

  // TODO: alternatively: make Axis extending LeafShape directly?
//  implicit def asLeafShape[A <: Axis]: A =>> (LeafShape.Eye >< A) = {
//
//    forAll[A].=>> { axis =>
//      Shape >|< axis
//    }
//  }
}
