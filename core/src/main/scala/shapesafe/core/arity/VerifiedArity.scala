package shapesafe.core.arity

import shapesafe.core.debugging.ExpressionType

/**
  * always successful, no need to verify
  */
trait VerifiedArity extends ArityType.Verifiable with ExpressionType.Leaf {}

object VerifiedArity {

  import ProveArity._

  implicit def endo[T <: VerifiedArity]: T |- T =
    ProveArity.forAll[T].=>>(identity[T])

//  implicit object Endo extends GenProof[VerifiedArity] {
//    override type In[T] = T
//    override type Out =
//
//  }

//  implicit class Endo[T <: VerifiedArity] extends GenProof[T] {
//
//    type Proof
//  }
}
