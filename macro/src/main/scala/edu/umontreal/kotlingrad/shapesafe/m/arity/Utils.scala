package edu.umontreal.kotlingrad.shapesafe.m.arity

import shapeless.Nat
import singleton.ops.ToInt

import scala.language.higherKinds

object Utils {

  type Op = singleton.ops.impl.Op

  // TODO: enable after this has been fixed:
  //  https://github.com/fthomas/singleton-ops/issues/148
  //  type Op = singleton.ops.impl.OpInt[_]

  type NatAsOp[N <: Nat] = ToInt[N]

  //  implicit def const[
  //      N1,
  //      N2,
  //      Fr[_, _]
  //  ](
  //      implicit
  //      major: Consts[N1, N2],
  //      minor: Fr[N1, N2] with Op
  //  ): Consts[N1, N2]#Op2Impl[Fr] = {
  //
  //    new major.Op2Impl[Fr]()
  //  }
}
