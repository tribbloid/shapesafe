package shapesafe.core.axis

import shapesafe.core.XString
import shapesafe.core.arity.Arity
import shapesafe.core.arity.ops.HasArity
import shapesafe.core.axis.Axis.:<<-
import shapeless.Witness

trait AxisLike extends HasArity {

  val nameW: Witness.Lt[XString]
  final type Name = nameW.T

  final def name: Name = nameW.value

  def nameless: Arity.^[_ArityType] = arityType.^

  def namedT[S <: XString](
      implicit
      name: Witness.Aux[S]
  ): _ArityType :<<- S = Axis(nameless, name)

  def named(name: Witness.Lt[XString]): _ArityType :<<- name.T = {

    namedT(name)
  }

  def :<<-(name: Witness.Lt[XString]): _ArityType :<<- name.T = namedT(name)
}
