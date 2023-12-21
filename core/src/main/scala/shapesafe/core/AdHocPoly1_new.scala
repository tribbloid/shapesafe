package shapesafe.core

import ai.acyclic.prover.commons.function.Symbolic
import shapeless.{::, HNil}

/**
  * Different from dotty's polymorphic function kind, which only supports parametric polymorphic
  */
trait AdHocPoly1_new[IUB, OUB] extends Symbolic.Poly1 {

  import Symbolic._

  type =>>[I, O] = Case[I :=> O]

  def forAll[I]: ForIO[I :: HNil, Any] = forI[I :: HNil]
}

object AdHocPoly1_new {}
