//package edu.umontreal.kotlingrad.shapesafe.m.arity.binary
//
//import edu.umontreal.kotlingrad.shapesafe.m.arity.{Operand, Proof}
//import singleton.ops.{==, Require}
//
//case class MayEqual[
//    -A1 <: Operand,
//    -A2 <: Operand
//](
//    a1: A1,
//    a2: A2
//) extends Operand {}
//
//object MayEqual {
//
//  // TODO : both of these signatures are not refined enough, it should contains 4 cases:
//  //  Const - Const -> Const[1]
//  //  Const - Unsafe -> Const[1]
//  //  Unsafe - Const -> Const[2]
//  //  Unsafe - Unsafe -> Unsafe
//
//  implicit class ProveInvar[A1 <: Operand, A2 <: Operand](self: MayEqual[A1, A2])(
//      implicit
//      prove1: A1 => Proof.Invar,
//      prove2: A2 => Proof.Invar
//  ) extends Proof {
//
//    val proof1: Proof = prove1(self.a1)
//    val proof2: Proof = prove2(self.a2)
//
//    override type Out = prr
//
//    override def out: ProveInvar.this.type = ???
//  }
//
//  def proveInvar[A1 <: Operand, A2 <: Operand](self: MayEqual[A1, A2])(
//      implicit
//      prove1: A1 => Proof.Invar,
//      prove2: A2 => Proof.Invar
//  ) = {
//
//    val proof1 = prove1(self.a1)
//    val proof2 = prove2(self.a2)
//
//    type Out = proof1.Out
//  }
//
//  implicit def unsafe[A1 <: Operand, A2 <: Operand](
//      implicit
//      domain: A1 IsUnsafe A2
//  ): MayEqual[A1, A2] = {
//
//    domain.ProbablyEqual
//  }
//
//  implicit def invar[N1, N2](
//      implicit
//      domain: IsInvar[N1, N2],
//      lemma: Require[N1 == N2]
//  ): MayEqual[Invar[N1], Invar[N2]] with Invar[_] = {
//
//    domain.Equal
//  }
//}
