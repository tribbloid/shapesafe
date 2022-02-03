package shapesafe.core.arity

import singleton.ops.ToString

/**
  * Irreducible
  */
trait LeafArity extends VerifiedArity {

  final override type _DebugSymbol = ToString[Notation]
}

object LeafArity {}
