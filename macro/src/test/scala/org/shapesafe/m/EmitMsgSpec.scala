//package org.shapesafe.m
//
//import com.tribbloids.graph.commons.testlib.BaseSpec
//
//class EmitMsgSpec extends BaseSpec {
//
//  it("can emit error") {
//
//    shouldNotCompile(
//      """EmitMsg.emit["ABC", EmitMsg.Error]""",
//      "ABC"
//    )
//
//    type TT = EmitMsg["ABC", EmitMsg.Error]
//    shouldNotCompile(
//      """implicitly[TT]""",
//      "ABC"
////      ".*"
//    )
//  }
//
//  it("can emit warning") {
//    EmitMsg.emit["ABC", EmitMsg.Warning]
//
//    type TT = EmitMsg["ABC", EmitMsg.Warning]
//    implicitly[TT] //(EmitMsg.emit)
//  }
//
//  it("can emit info") {
//    EmitMsg.emit["ABC", EmitMsg.Info]
//
//    type TT = EmitMsg["ABC", EmitMsg.Info]
//    implicitly[TT] //(EmitMsg.emit)
//  }
//}
