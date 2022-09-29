package shapesafe.m

import ai.acyclic.graph.commons.testlib.BaseSpec
import singleton.ops.ToString

class GenericMsgEmitterSpec extends BaseSpec {

  import GenericMsgEmitterSpec._

  it("byOnlyInstance") {

    shouldNotCompile(
      """emitError.byOnlyInstance["ABC"]""",
      "ABC"
    )

    type RR = "ABC"
    type TT = GenericMsgEmitter[RR, GenericMsgEmitter.Error]
    shouldNotCompile(
      """implicitly[TT]""",
      "ABC"
    )
  }

  it("byOp") {

    type T = ToString["ABC"]

//    val op = implicitly[ToString["ABC"]]

    shouldNotCompile(
      """emitError.byOp[ToString["ABC"]]""",
      "ABC"
    )

    shouldNotCompile(
      """emitError.byOp[T]""",
      "ABC"
    )

//    val v = doOp[ToString["ABC"]]

//    shouldNotCompile(
//      """run[ToString["ABC"]]""",
//      "ABC"
//    )
  }

  // TODO: how to validate compile-time emitted message by assertion?
  it("can emit warning") { // TODO: how to de-verbose?
    emitWarning.byOnlyInstance["ABC"]

    type TT = GenericMsgEmitter["ABC", GenericMsgEmitter.Warning]
    implicitly[TT] // (EmitMsg.emit)
  }

  it("can emit info") {
    emitInfo.byOnlyInstance["ABC"]

    type TT = GenericMsgEmitter["ABC", GenericMsgEmitter.Info]
    implicitly[TT] // (EmitMsg.emit)
  }
}

object GenericMsgEmitterSpec {

  val emitError: GenericMsgEmitter.Level[GenericMsgEmitter.Error] = GenericMsgEmitter[GenericMsgEmitter.Error]
  val emitWarning: GenericMsgEmitter.Level[GenericMsgEmitter.Warning] = GenericMsgEmitter[GenericMsgEmitter.Warning]
  val emitInfo: GenericMsgEmitter.Level[GenericMsgEmitter.Info] = GenericMsgEmitter[GenericMsgEmitter.Info]

  // TODO: blocked by inline feature: https://stackoverflow.com/questions/67526001/in-scala-can-a-function-be-defined-to-have-pass-by-ast-parameter-such-that-the
//  def doOp[T <: Op](
//      implicit
//      op: T
//  ): EmitMsg[T, EmitMsg.Error] = {
//    emitError.byOp[T](op)
//  }
}
