//package org.shapesafe.core.debugging
//
//import com.tribbloids.graph.commons.util.viz.TypeViz
//import org.shapesafe.BaseSpec
//import org.shapesafe.core.debugging.InfoCT.Fail
//import org.shapesafe.core.debugging.ProveInfo.|-
//import shapeless.Witness
//import singleton.ops.+
// TODO: move it somewhere else
//import scala.collection.mutable
//
//class ProveInfoSpec extends BaseSpec {
//
//  import ProveInfo._
//  import ProveInfoSpec._
//
//  describe("fallback") {
//
//    import Dummy1._
//
//    it("explicitly") {
//
//      val p = fallback1[Src]
//      val v = p.valueOf(null)
//
//      TypeViz[v._Info].should_=:=(gd)
//    }
//
//    it("implicitly") {
//
//      val p = forAll[Src].summon
//
//      val v = p.valueOf(null)
//      TypeViz[v._Info].should_=:=(gd)
//    }
//
//    it("debugger") {
//
//      shouldNotCompile(
//        """implicitly[DebuggerExample[Src]]""",
//        """.*(HashMap\[String,Int\])"""
//      )
//    }
//  }
//}
//
//object ProveInfoSpec {
//
//  class DebuggerExample[A]
//
//  object DebuggerExample {
//
//    implicit def debug[
//        A1,
//        I1 <: InfoCT
//    ](
//        implicit
//        n1: A1 |- I1,
//        fail: Fail[InfoCT.prefix.T + I1#_Info]
//    ): DebuggerExample[A1] = new DebuggerExample[A1]
//  }
//
//  object Dummy1 extends HasInfoFallback[Any] {
//
//    type Src = mutable.HashMap[String, Int]
//    val gd = TypeViz.infer(Witness("""HashMap[String,Int]""").value)
//  }
//}
