package org.shapesafe.core.axis

import com.tribbloids.graph.commons.util.IDMixin
import org.shapesafe.core.arity.Arity.HasArity
import org.shapesafe.core.arity.{Arity, ArityAPI}
import org.shapesafe.core.axis.Axis.AxisLike
import shapeless.Witness
import shapeless.labelled.FieldType

import scala.language.implicitConversions

trait Axis extends AxisLike with IDMixin {
  //TODO:; can be a subclass of shapeless KeyTag

  final type Field = FieldType[Name, ArityInner]
  final def asField: Field = arityInner.asInstanceOf[Field]

  type AxisSelf >: this.type <: Axis
  final def axisSelf: AxisSelf = this: AxisSelf

  override protected lazy val _id: Any = (arityInner, name)
}

object Axis {

  type ->>[N <: String, D] = FieldType[N, D]
  type UB_->> = (_ <: String) ->> Arity

  // TODO: N can be eliminated
  final class :<<-[
      A <: Arity,
      N <: String
  ](
      val arityInner: A,
      val nameSingleton: Witness.Aux[N]
  ) extends Axis
//      with KeyTag[N, D :<<- N]
      // TODO: remove? FieldType[] has some black magic written in macro
      {

    type ArityInner = A

    type AxisSelf = ArityInner :<<- Name

    override lazy val toString: String = {
      if (name.isEmpty) s"$arityInner"
      else s"$arityInner :<<- $name"
    }
  }

  def apply(
      value: ArityAPI,
      name: Witness.Lt[String]
  ): :<<-[value.ArityInner, name.T] = {

    new :<<-(value.arityInner, name)
  }

  trait AxisLike extends HasArity {

    val nameSingleton: Witness.Lt[String]
    final type Name = nameSingleton.T

    final def name: Name = nameSingleton.value

    def nameless: ArityAPI.^[ArityInner] = arityInner.^

    def namedT[S <: String](
        implicit
        name: Witness.Aux[S]
    ): ArityInner :<<- S = Axis(nameless, name)

    def named(name: Witness.Lt[String]): ArityInner :<<- name.T = {

      namedT(name)
    }

    def :<<-(name: Witness.Lt[String]): ArityInner :<<- name.T = namedT(name)
  }
}
