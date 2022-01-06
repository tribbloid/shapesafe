package shapesafe.core.axis

import shapesafe.core.XString
import shapesafe.core.arity.ArityAPI
import shapesafe.core.arity.ops.HasArity
import shapesafe.core.axis.Axis.:<<-
import shapeless.Witness

trait AxisLike extends HasArity {

  val nameW: Witness.Lt[XString]
  final type Name = nameW.T

  final def name: Name = nameW.value

  def nameless: ArityAPI.^[_Arity] = arity.^

  def namedT[S <: XString](
      implicit
      name: Witness.Aux[S]
  ): _Arity :<<- S = Axis(nameless, name)

  def named(name: Witness.Lt[XString]): _Arity :<<- name.T = {

    namedT(name)
  }

  def :<<-(name: Witness.Lt[XString]): _Arity :<<- name.T = namedT(name)
}
