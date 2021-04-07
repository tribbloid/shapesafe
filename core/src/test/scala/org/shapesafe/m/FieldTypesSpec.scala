//package org.shapesafe.m
//
//import com.tribbloids.graph.commons.testlib.BaseSpec
//import com.tribbloids.graph.commons.util.viz.TypeViz
//import shapeless.labelled.FieldType
//
//class FieldTypesSpec extends BaseSpec {
//
//  import FieldTypesSpec._
//  import shapeless._
//
//  it("poly") {
//
//    val cc = FieldTypes.typeablePoly.cse[java.lang.String]
//    val cc2 = FieldTypes.typeablePoly.cse[Map[String, Int]]
//
//    TypeViz.infer(cc2).should_=:=()
//  }
//
//  it("ground truth") {
//
//    val v = implicitly[FieldTypes.Aux[Prod, FieldType["Int", String] :: FieldType["String", Int] :: HNil]]
//
//    TypeViz.infer(v).should_=:=()
//  }
//}
//
//object FieldTypesSpec {
//
//  case class Prod(
//      a: Int,
//      b: String
//  )
//}
