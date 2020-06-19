//package edu.umontreal.kotlingrad.shapesafe.m.arity.binary
//
//import edu.umontreal.kotlingrad.shapesafe.BaseSpec
//import edu.umontreal.kotlingrad.shapesafe.m.arity.Arity
//import shapeless.Witness
//
//class MayEqualSpec extends BaseSpec {
//
//  it("can summon") {
//
//    implicit val a1: Arity.FromLiteral[Witness.`7`.T] = Arity(7)
//
//    // different ways of summoning
//    val s1 = MayEqual.invar[Witness.`7`.T, Witness.`7`.T]
//    val s2 = implicitly[Arity.FromLiteral[Witness.`7`.T] MayEqual Arity.FromLiteral[Witness.`7`.T]]
//    val s3 = implicitly[a1.type MayEqual a1.type]
//    val s4 = implicitly[a1.Out MayEqual a1.Out]
//  }
//}
