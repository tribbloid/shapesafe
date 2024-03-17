package shapesafe.m

import singleton.ops.RequireMsg

object Emit {

  val FALSE: false = false

  type Error[T] = RequireMsg[FALSE.type, T]
//  type Error[T] = RequireMsg[FALSE.T, "\n" + T]
//  type Error[T] = GenericMsgEmitter[T, GenericMsgEmitter.Abort]

  //  type Warning[T] = RequireMsgSym[FALSE.T, T, singleton.ops.Warn]
  type Warning[T] = GenericMsgEmitter[T, GenericMsgEmitter.Warning]

  type Info[T] = GenericMsgEmitter[T, GenericMsgEmitter.Info]
}
