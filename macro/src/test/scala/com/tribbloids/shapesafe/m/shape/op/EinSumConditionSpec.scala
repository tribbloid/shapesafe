package com.tribbloids.shapesafe.m.shape.op

import com.tribbloids.shapesafe.BaseSpec
import com.tribbloids.shapesafe.m.arity.Arity
import shapeless.HNil

class EinSumConditionSpec extends BaseSpec {

  import shapeless.syntax.singleton.mkSingletonOps
  import shapeless.record._

  val ii = "i" ->> Arity(3)

  describe("can append") {

    it("HNil") {

      val out = EinSumCondition.apply(HNil -> ii)

      out.fields.toString.shouldBe("(i,FromLiteral: 3) :: HNil")
    }

    describe("new name to a Record") {

      it("1") {

        val existing = ("a" ->> Arity(4)) :: HNil

        val out = EinSumCondition.apply(existing -> ii)

        out.fields.toString.shouldBe("(i,FromLiteral: 3) :: (a,FromLiteral: 4) :: HNil")
      }

      it("2") {

        val existing = ("a" ->> Arity(4)) :: ("b" ->> Arity(5)) :: HNil

        val out = EinSumCondition.apply(existing -> ii)

        out.fields.toString.shouldBe("(i,FromLiteral: 3) :: (a,FromLiteral: 4) :: (b,FromLiteral: 5) :: HNil")
      }
    }

    describe("old name to a Record that contains identical pair") {

      it(" 1") {

        val existing = ("i" ->> Arity(3)) :: HNil

        val out = EinSumCondition.apply(existing -> ii)

        out.fields.toString.shouldBe("(i,FromLiteral: 3) :: (i,FromLiteral: 3) :: HNil")
      }

      it("2") {

        val existing = ("i" ->> Arity(3)) :: ("j" ->> Arity(4)) :: HNil

        val out = EinSumCondition.apply(existing -> ii)

        out.fields.toString.shouldBe("(i,FromLiteral: 3) :: (i,FromLiteral: 3) :: (j,FromLiteral: 4) :: HNil")
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
