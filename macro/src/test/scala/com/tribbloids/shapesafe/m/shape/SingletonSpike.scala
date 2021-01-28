//package com.tribbloids.shapesafe.m.shape
//
//import com.tribbloids.graph.commons.util.viz.VizType
//import com.tribbloids.shapesafe.BaseSpec
//import com.tribbloids.shapesafe.m.shape
//import shapeless.ops.hlist.ZipWithKeys
//import shapeless.{::, HList, SingletonProductArgs, Witness}
//
//// TODO: it doesn't work
////  blocked by https://github.com/milessabin/shapeless/issues/1072
//class SingletonSpike extends BaseSpec {
//
//  import shapeless.syntax.singleton._
//
//  object SingletonBroker extends SingletonProductArgs {
//
//    def applyProduct[H <: HList](v: H)(
//        implicit withKeys: ZipWithKeys[H, H]
//    ): withKeys.Out = {
//
//      v.zipWithKeys(v)(withKeys)
//    }
//  }
//
//  it("should works") {
//
//    val v1 = SingletonBroker("a", 1)
////    val v1 = SingletonBroker.applyProduct("a".narrow :: HNil)
//
//    VizType.infer(v1).shouldBe()
//  }
//
//  it("should not work") {
//
//    val x = 1
////    val v1 = SingletonBroker("a", x)
//  }
//
//  describe("names") {
//
//    object NamesBroker extends SingletonProductArgs {
//
//      def applyProduct[H <: HList, O <: Names.Impl](v: H)(
//          implicit
//          toNames: Names.FromStatic.Case[H],
//          withKeys: ZipWithKeys[H, H]
//      ): (toNames.Out, withKeys.Out) = {
//
//        toNames.apply(v) -> withKeys.apply(v)
//      }
//    }
//
//    it("should works") {
//
//      val v1 = NamesBroker("a")
////      val v1 = NamesBroker.applyProduct("a".narrow :: HNil)
//
//      VizType.infer(v1._1).shouldBe()
//
//      VizType.infer(v1._2).shouldBe()
//    }
//
//    object NamesBroker2 extends SingletonProductArgs {
//
//      def applyProduct[H <: HList, O <: Names.Impl](v: H)(
//          implicit
//          toNames: shape.SingletonSpike.FromStatic2.Case[H],
//          withKeys: ZipWithKeys[H, H]
//      ): (toNames.Out, withKeys.Out) = {
//
//        toNames.apply(v) -> withKeys.apply(v)
//      }
//    }
//
//    it("2") {
//
//      val v1 = NamesBroker2("a")
//      //      val v1 = NamesBroker.applyProduct("a".narrow :: HNil)
//
//      SingletonBroker.apply("a", "b")
//      val a = "A"
//      SingletonBroker.apply(a, "b")
//      SingletonBroker.apply(SingletonSpike.a, "b")
//
//      VizType.infer(v1._1).shouldBe()
//
//      VizType.infer(v1._2).shouldBe()
//    }
//  }
//}
//
//object SingletonSpike {
//
//  val a = "A"
//
//  object FromStatic2 extends Names.HListConverter {
//
//    import Names._
//
//    implicit def recursive[
//        TAIL <: HList,
//        HEAD <: UpperBound
//    ](
//        implicit
//        forTail: Case[TAIL],
//        singleton: Witness.Aux[HEAD]
//    ): (HEAD :: TAIL) ==> (forTail.Out >< HEAD) =
//      apply[HEAD :: TAIL].build { v =>
//        val prev = forTail(v.tail)
//
//        new ><(prev, v.head)
//      }
//  }
//}
