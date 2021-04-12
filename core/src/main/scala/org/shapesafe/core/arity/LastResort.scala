//package org.shapesafe.core.arity
//
//import org.shapesafe.core.arity.Arity.CanPeek
//import org.shapesafe.core.util.~
//import org.shapesafe.m.InfoLiteral
//import singleton.ops.ToString
//
//trait LastResort {
//  // TODO: useless, scala implicit priority is broken
//
//  import ProveArity.Factory._
//  import LastResort._
//
//  implicit def lastResortPeek[
//      A <: Arity
//  ](
//      implicit
//      cannotPeek: ~[A <:< CanPeek],
//      getInfo: InfoLiteral.Type.From[A]
//  ): A =>> PeekDelegate[A, getInfo.Out] = ProveArity.forAll[A].=>> { v =>
//    PeekDelegate[A, getInfo.Out](v)
//  }
//}
//
//object LastResort {
//
//  case class PeekDelegate[A <: Arity, O](self: Arity) extends Arity {
//
//    override def runtimeArity: Int = self.runtimeArity
//
//    type _Out = O
//
//    override type _Peek = ToString[_Out]
//  }
//}
