package org.shapesafe.m

import org.shapesafe.graph.commons.testlib.BaseSpec
import singleton.ops.ToString

class EmitMsgSpec extends BaseSpec {

  import EmitMsgSpec._

  it("weakly") {

    shouldNotCompile(
      """emitError.weakly["ABC"]""",
      "ABC"
    )

    type RR = "ABC"
    type TT = EmitMsg[RR, EmitMsg.Error]
//    shouldNotCompile(
//      """implicitly[TT]""",
//      "ABC"
//    )
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

//  it("byTypeable") {
//
//    type T = ToString["ABC"]
//
//    shouldNotCompile(
//      """emitError.byTypeable[ToString["ABC"]]""",
//      "ABC"
//    )
//
//    shouldNotCompile(
//      """emitError.byTypeable[T]""",
//      "ABC"
//    )
//
//    def run[T](
//        implicit
//        op: Typeable[T]
//    ): EmitMsg[T, EmitMsg.Error] = {
//      emitError.byTypeable[T](op)
//    }
//
//    val v = run[ToString["ABC"]]
//
////    shouldNotCompile(
////      """run[ToString["ABC"]]""",
////      "ABC"
////    )
//  }

  // TODO: how to validate compile-time emitted message by assertion?
  it("can emit warning") { // TODO: how to de-verbose?
    emitWarning.weakly["ABC"]

    type TT = EmitMsg["ABC", EmitMsg.Warning]
    implicitly[TT] //(EmitMsg.emit)
  }

  it("can emit info") {
    emitInfo.weakly["ABC"]

    type TT = EmitMsg["ABC", EmitMsg.Info]
    implicitly[TT] //(EmitMsg.emit)
  }
}

object EmitMsgSpec {

  val emitError: EmitMsg.Level[EmitMsg.Error] = EmitMsg[EmitMsg.Error]
  val emitWarning: EmitMsg.Level[EmitMsg.Warning] = EmitMsg[EmitMsg.Warning]
  val emitInfo: EmitMsg.Level[EmitMsg.Info] = EmitMsg[EmitMsg.Info]

//  val op = implicitly[ToString["ABC"]]

  // TODO: blocked by inline feature: https://stackoverflow.com/questions/67526001/in-scala-can-a-function-be-defined-to-have-pass-by-ast-parameter-such-that-the
//  def doOp[T <: Op](
//      implicit
//      op: T
//  ): EmitMsg[T, EmitMsg.Error] = {
//    emitError.byOp[T](op)
//  }
}
