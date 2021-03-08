package org.shapesafe.core.arity

import shapeless.Nat
import singleton.ops.ToInt
import singleton.ops.impl.{std, OpId, OpMacro}
import singleton.twoface.impl.TwoFaceAny

object Utils {

  type Op = singleton.ops.impl.Op

  // TODO: enable after this has been fixed:
  //  https://github.com/fthomas/singleton-ops/issues/148
  //  type Op = singleton.ops.impl.OpInt[_]

  type NatAsOp[N <: Nat] = ToInt[N]

  type RequireLike[S <: Op] = OpMacro[OpId.Require, S, _, _]

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

  type IntSh[??[_, _] <: Op] = TwoFaceAny.Int.Shell2[??, Int, std.Int, Int, std.Int]
}
