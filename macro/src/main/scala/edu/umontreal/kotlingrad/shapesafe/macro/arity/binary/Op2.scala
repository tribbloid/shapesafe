package edu.umontreal.kotlingrad.shapesafe.`macro`.arity.binary

import edu.umontreal.kotlingrad.shapesafe.`macro`.arity.Utils.Op
import edu.umontreal.kotlingrad.shapesafe.`macro`.arity.Proof.Invar
import edu.umontreal.kotlingrad.shapesafe.`macro`.arity.{Expr, Proof, Utils}
import shapeless.Lazy
import singleton.ops

import scala.language.higherKinds

trait Op2[
    -A1 <: Proof,
    -A2 <: Proof,
    Fr[X, Y] <: Op
] extends Expr {

  //    type Out <: Arity
  //    def out(a1: A1, a2: A2): Out
}

object Op2 {

  implicit def unsafe[
      A1 <: Proof,
      A2 <: Proof,
      Fr[X, Y] <: Op
  ](
      implicit domain: A1 UnsafeDomain A2
  ): Op2[A1, A2, Fr] = {

    new domain.Op2Impl[Fr]()
  }

//  implicit def invar[
//      N1,
//      N2,
//      Fr[X, Y] <: Op
//  ](
//      implicit
//      domain: Lazy[N1 InvarDomain N2],
//      lemma: Fr[N1, N2]
//  ): Op2[Invar[N1], Invar[N2], Fr] with Invar[Fr[N1, N2]] = {
//
//    new domain.value.Op2Impl[Fr]()
//  }

  implicit def invar[
      N1,
      N2,
      Fr[X, Y] <: Op
  ](
      implicit
      domain: N1 InvarDomain N2,
      lemma: Fr[N1, N2]
  ): Op2[Invar[N1], Invar[N2], Fr] with Invar[Fr[N1, N2]] = {

    new domain.Op2Impl[Fr]()
  }

  type ^+[-A1 <: Proof, -A2 <: Proof] = Op2[A1, A2, ops.+]

  type ^-[-A1 <: Proof, -A2 <: Proof] = Op2[A1, A2, ops.-]

  type ^*[-A1 <: Proof, -A2 <: Proof] = Op2[A1, A2, ops.*]

  type ^/[-A1 <: Proof, -A2 <: Proof] = Op2[A1, A2, ops./]
}
