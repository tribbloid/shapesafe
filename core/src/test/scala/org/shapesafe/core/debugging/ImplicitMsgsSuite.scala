//package org.shapesafe.core.debugging
//
//import org.shapesafe.BaseSpec
//import shapeless.ops.hlist.ZipWithKeys
//import shapeless.{::, HNil}
//import singleton.ops.+
//
//class ImplicitMsgsSuite extends BaseSpec {
//
//  import ImplicitMsgsSuite._
//
//  it("can summon") {
//
//    implicitly[NotFound[Int =:= Int]]
//  }
//
//  it("can generate the correct error message") {
//
//    shouldNotCompile(
//      """implicitly[NotFound[Int =:= String]]""",
//      ".*(Dummy).*"
//    )
//  }
//
//  describe("indirectly 1") {
//
//    it("can summon") {
//
//      implicitly[Indirect[Int]]
//    }
//
//    it("can generate the correct error message") {
//
//      //    implicitly[WMsg[Int =:= String]]
//
//      shouldNotCompile(
//        """implicitly[Indirect[String]]""",
//        ".*(Dummy).*"
//      )
//    }
//  }
//
//  describe(" ... 2") {
//
//    it("can summon") {
//
//      implicitly[Indirect2[Int =:= Int]]
//
//      implicitly[
//        Indirect2[
//          ZipWithKeys[
//            "A" :: "B" :: HNil,
//            1 :: 2 :: HNil
//          ],
//        ]
//      ]
//    }
//
//    it("can generate the correct error message") {
//
//      //    implicitly[WMsg[Int =:= String]]
//
//      shouldNotCompile(
//        """implicitly[Indirect[Int =:= String]]""",
//        ".*(Dummy).*"
//      )
//    }
//  }
//}
//
//object ImplicitMsgsSuite {
//
//  type MSG = "  Dum" + "my  "
//
//  type NotFound[T] = ErrorIfNotFound[
//    T,
//    MSG
//  ]
//
//  trait Indirect[T]
//
//  object Indirect {
//
//    implicit def pt[T](
//        implicit
//        ev: NotFound[T =:= Int]
//    ): Indirect[T] = new Indirect[T] {}
//  }
//
//  trait Indirect2[T]
//
//  object Indirect2 {
//
//    implicit def pt[T](
//        implicit
//        ev: NotFound[T]
//    ): Indirect2[T] = new Indirect2[T] {}
//  }
//}
