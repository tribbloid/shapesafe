package shapesafe.core.arity.ops

import shapesafe.core.arity.{Arity, ArityType}

trait HasArity {

  type _ArityType <: ArityType
  def arityType: _ArityType

  def arity: Arity.^[_ArityType] = arityType.^
}
