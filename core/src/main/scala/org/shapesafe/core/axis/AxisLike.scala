package org.shapesafe.core.axis

import org.shapesafe.core.arity.ArityAPI
import org.shapesafe.core.arity.ops.HasArity
import org.shapesafe.core.axis.Axis.:<<-
import shapeless.Witness

trait AxisLike extends HasArity {

  val nameW: Witness.Lt[String with Singleton]
  final type Name = nameW.T

  final def name: Name = nameW.value

  def nameless: ArityAPI.^[_Arity] = arity.^

  def namedT[S <: String with Singleton](
      implicit
      name: Witness.Aux[S]
  ): _Arity :<<- S = Axis(nameless, name)

  def named(name: Witness.Lt[String with Singleton]): _Arity :<<- name.T = {

    namedT(name)
  }

  def :<<-(name: Witness.Lt[String with Singleton]): _Arity :<<- name.T = namedT(name)
}
