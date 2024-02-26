package shapesafe

import ai.acyclic.prover.commons.function.PreDef

package object core {

  type XInt = Int with Singleton
  type XString = String with Singleton

  type AdHocPoly1 = PreDef.Poly
}
