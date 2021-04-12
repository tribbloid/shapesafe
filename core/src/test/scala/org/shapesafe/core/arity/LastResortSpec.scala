//package org.shapesafe.core.arity
//
//import com.tribbloids.graph.commons.util.viz.TypeViz
//import org.shapesafe.BaseSpec
//
//class LastResortSpec extends BaseSpec {
//
//  it("Unprovable") {
//
//    val v = Arity.Unprovable.^
//    val k = Arity.lastResortPeek[v._Arity].valueOf(v)
//
//    TypeViz[k._Out].should_=:=()
//
//    shouldNotCompile(
//      """v.peek""",
//      ".*(Arity.Unprovable.type)"
//    )
//
//  }
//}
