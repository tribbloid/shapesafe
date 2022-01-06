package shapesafe.core.axis

import shapesafe.BaseSpec
import shapesafe.core.arity.Arity
import shapeless.HNil

class NewNameAppenderSpec extends BaseSpec {

  import shapeless.record._
  import shapeless.syntax.singleton.mkSingletonOps

  Record // Don't remove! IDE can't figure out the proper import

  val ii = "i" ->> Arity(3).arity

  describe("can append") {

    it("HNil") {

      val out = NewNameAppender.apply(HNil -> ii)

      out.fields.toString.shouldBe("(i,3:Literal) :: HNil")
    }

    describe("new name to a Record") {

      it("1") {

        val existing = ("a" ->> Arity(4).arity) :: HNil

        val out = NewNameAppender.apply(existing -> ii)

        out.fields.toString.shouldBe("(i,3:Literal) :: (a,4:Literal) :: HNil")
      }

      it("2") {

        val existing = ("a" ->> Arity(4).arity) :: ("b" ->> Arity(5).arity) :: HNil

        val out = NewNameAppender.apply(existing -> ii)

        out.fields.toString.shouldBe("(i,3:Literal) :: (a,4:Literal) :: (b,5:Literal) :: HNil")
      }
    }
  }
}
