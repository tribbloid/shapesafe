package shapesafe.core.axis

import shapeless.HNil
import shapesafe.BaseSpec
import shapesafe.core.arity.Arity

class NewNameAppenderSpec extends BaseSpec {

  import shapeless.record._
  import shapeless.syntax.singleton.mkSingletonOps

  Record // Don't remove! IDE can't figure out the proper import

  val ii = "i" ->> Arity(3).arityType

  val jj = "" ->> Arity(3).arityType

  describe("can append") {

    it("HNil") {

      val out = NewNameAppender.apply(HNil -> ii)

      out.fields.toString.shouldBe("(i,3:Literal) :: HNil")
    }

    describe("new name to a Record") {

      it("1") {

        val existing = ("a" ->> Arity(4).arityType) :: HNil

        val out = NewNameAppender.apply(existing -> ii)

        out.fields.toString.shouldBe("(i,3:Literal) :: (a,4:Literal) :: HNil")
      }

      it("2") {

        val existing = ("a" ->> Arity(4).arityType) :: ("b" ->> Arity(5).arityType) :: HNil

        val out = NewNameAppender.apply(existing -> ii)

        out.fields.toString.shouldBe("(i,3:Literal) :: (a,4:Literal) :: (b,5:Literal) :: HNil")
      }
    }

    // TODO: can't decide the design at the moment
//    describe("arity with no name to a Record") {
//
//      it("1") {
//
//        val existing = ("" ->> Arity(4).arityType) :: HNil
//
//        val out = NewNameAppender.apply(existing -> jj)
//
//        out.fields.toString.shouldBe("(,3:Literal) :: (,4:Literal) :: HNil")
//      }
//    }
  }
}
