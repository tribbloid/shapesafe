package edu.umontreal.kotlingrad.shapesafe

class BaseSpecSpec extends BaseSpec {

  val value = 1

  describe("shouldNotCompile should") {

//    it("throws an exception if compilation succeeds") {
//
//      intercept[CloneNotSupportedException] {
//
//        shouldNotCompile {
//          "val k = value"
//        }
//      }
//    }

    it("pass if compilation fails") {

      shouldNotCompile {
        "val k = dummy"
      }
    }
  }
}
