package edu.umontreal.kotlingrad.shapesafe.arity.ops

import edu.umontreal.kotlingrad.shapesafe.arity.Arity
import edu.umontreal.kotlingrad.shapesafe.arity.ops.BinaryOps.Facet
import edu.umontreal.kotlingrad.shapesafe.arity.proof.MayEqual

import scala.language.higherKinds

trait BinaryUnsafe[A1 <: Arity, A2 <: Arity] extends BinaryOps[A1, A2] {

  class FacetImpl[ConstOp[_, _]] extends Facet {

    override type Out = Arity.FromInt_Unsafe
    override def out(v: Int): Arity.FromInt_Unsafe = Arity.FromInt_Unsafe(v)
  }

  override def getFacet[ConstOp[_, _]]: FacetImpl[ConstOp] = new FacetImpl[ConstOp]

  def ProbablyEqual: A1 MayEqual A2
}

trait BinaryUnsafe_Implicits0 {

  class _0[A1 <: Arity.Unsafe, A2 <: Arity] extends BinaryUnsafe[A1, A2] {

    override case object ProbablyEqual extends (A1 MayEqual A2) {

      override type Out = A2
      override def _out(a1: A1, a2: A2): Out = a2
    }
  } // of lower priority

  implicit def _0[A1 <: Arity.Unsafe, A2 <: Arity]: _0[A1, A2] = new _0[A1, A2]
}

object BinaryUnsafe extends BinaryUnsafe_Implicits0 {

  class _1[A1 <: Arity, A2 <: Arity.Unsafe]() extends BinaryUnsafe[A1, A2] {

    override case object ProbablyEqual extends (A1 MayEqual A2) {

      override type Out = A1
      override def _out(a1: A1, a2: A2): Out = a1
    }
  }

  implicit def _1[A1 <: Arity, A2 <: Arity.Unsafe]: _1[A1, A2] = new _1[A1, A2]()
}
