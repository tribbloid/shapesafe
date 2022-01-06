package shapesafe.core.arity.ops

import shapesafe.core.arity.Arity

trait HasArity {

  type _Arity <: Arity
  def arity: _Arity
}
