package org.shapesafe.core.axis

import com.tribbloids.graph.commons.util.IDMixin
import org.shapesafe.core.arity.ops.HasArity
import org.shapesafe.core.arity.{Arity, ArityAPI}
import org.shapesafe.core.axis.Axis.AxisLike
import shapeless.Witness
import shapeless.labelled.FieldType

import scala.language.implicitConversions

trait Axis extends AxisLike with IDMixin {
  //TODO:; can be a subclass of shapeless KeyTag

  final type Field = FieldType[Name, _Arity]
  final def asField: Field = arity.asInstanceOf[Field]

  type _Axis >: this.type <: Axis
  final def axis: _Axis = this: _Axis

  override protected lazy val _id: Any = (arity, name)
}

object Axis {

  type ->>[N <: String, D] = FieldType[N, D]
  type UB_->> = (_ <: String) ->> Arity

  // TODO: N can be eliminated
  final class :<<-[
      A <: Arity,
      N <: String
  ](
      val arity: A,
      val nameSingleton: Witness.Aux[N]
  ) extends Axis
//      with KeyTag[N, D :<<- N]
      // TODO: remove? FieldType[] has some black magic written in macro
      {

    type _Arity = A

    type _Axis = _Arity :<<- Name

    override lazy val toString: String = {
      if (name.isEmpty) s"$arity"
      else s"$arity :<<- $name"
    }
  }

  def apply(
      value: ArityAPI,
      name: Witness.Lt[String]
  ): :<<-[value._Arity, name.T] = {

    new :<<-(value.arity, name)
  }

  trait AxisLike extends HasArity {

    val nameSingleton: Witness.Lt[String]
    final type Name = nameSingleton.T

    final def name: Name = nameSingleton.value

    def nameless: ArityAPI.^[_Arity] = arity.^

    def namedT[S <: String](
        implicit
        name: Witness.Aux[S]
    ): _Arity :<<- S = Axis(nameless, name)

    def named(name: Witness.Lt[String]): _Arity :<<- name.T = {

      namedT(name)
    }

    def :<<-(name: Witness.Lt[String]): _Arity :<<- name.T = namedT(name)
  }
}
