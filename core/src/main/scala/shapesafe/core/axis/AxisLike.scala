package shapesafe.core.axis

import ai.acyclic.prover.commons.refl.XString
import shapesafe.core.arity.Arity
import shapesafe.core.arity.ops.HasArity
import shapesafe.core.axis.Axis.:<<-

trait AxisLike extends HasArity {

  val nameW: XString
  final type Name = nameW.type

  final def name: Name = nameW

  def nameless: Arity.^[_ArityType] = arityType.^

  def namedT[S <: XString](
      implicit
      name: S
  ): _ArityType :<<- S = Axis(nameless, name)

  def named[S <: XString](name: S): _ArityType :<<- S = {

    namedT(name)
  }

  def :<<-[S <: XString](name: S): _ArityType :<<- S = namedT(name)
}
