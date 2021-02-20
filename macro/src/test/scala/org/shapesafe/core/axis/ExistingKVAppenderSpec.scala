package org.shapesafe.core.axis

import org.shapesafe.BaseSpec
import org.shapesafe.core.arity.Arity
import shapeless.HNil

class ExistingKVAppenderSpec extends BaseSpec {

  import shapeless.syntax.singleton.mkSingletonOps
  import shapeless.record._

  val ii = "i" ->> Arity(3)

  describe("can append") {

    describe("old name to a Record that contains identical pair") {

      it(" 1") {

        val existing = ("i" ->> Arity(3)) :: HNil

        val out = ExistingKVAppender.apply(existing -> ii)

        out.fields.toString.shouldBe("(i,3:Literal) :: (i,3:Literal) :: HNil")
      }

      it("2") {

        val existing = ("i" ->> Arity(3)) :: ("j" ->> Arity(4)) :: HNil

        val out = ExistingKVAppender.apply(existing -> ii)

        out.fields.toString.shouldBe("(i,3:Literal) :: (i,3:Literal) :: (j,4:Literal) :: HNil")
      }
    }

  }

  describe("cannot append if") {

    describe("old name to a Record that contains identical key but different value") {

      it(" 1") {

        val existing = ("i" ->> Arity(4)) :: HNil

        shouldNotCompile(
          """EinSumCondition.apply(existing -> ii)"""
        )

      }

      it("2") {

        val existing = ("i" ->> Arity(4)) :: ("j" ->> Arity(3)) :: HNil

        shouldNotCompile(
          """val out = EinSumCondition.apply(existing -> ii)"""
        )
      }

      it("3") {

        val existing = ("j" ->> Arity(3)) :: ("i" ->> Arity(4)) :: HNil

        shouldNotCompile(
          """EinSumCondition.apply(existing -> ii)"""
        )
      }
    }
  }
}
