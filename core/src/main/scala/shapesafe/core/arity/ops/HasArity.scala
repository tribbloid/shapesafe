package shapesafe.core.arity.ops

import shapesafe.core.arity.ArityType

trait HasArity {

  type _ArityType <: ArityType
  def arityType: _ArityType
}
