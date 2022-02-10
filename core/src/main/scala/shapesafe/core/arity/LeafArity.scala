package shapesafe.core.arity

import singleton.ops.ToString

/**
  * Irreducible
  */
trait LeafArity extends VerifiedArity {

  final override type _SymbolLit = ToString[Notation]
}

object LeafArity {}
