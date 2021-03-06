package org.shapesafe.core.axis

import org.shapesafe.core.arity.ArityAPI
import org.shapesafe.core.arity.ops.HasArity
import org.shapesafe.core.axis.Axis.:<<-
import shapeless.Witness

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
