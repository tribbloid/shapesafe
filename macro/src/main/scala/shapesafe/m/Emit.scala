package shapesafe.m

import shapeless.Witness
import singleton.ops.RequireMsg

object Emit {

  val FALSE = Witness(false)

  type Error[T] = RequireMsg[FALSE.T, T]
//  type Error[T] = RequireMsg[FALSE.T, "\n" + T]
//  type Error[T] = GenericMsgEmitter[T, GenericMsgEmitter.Abort]

  //  type Warning[T] = RequireMsgSym[FALSE.T, T, singleton.ops.Warn]
  type Warning[T] = GenericMsgEmitter[T, GenericMsgEmitter.Warning]

  type Info[T] = GenericMsgEmitter[T, GenericMsgEmitter.Info]
}
