package com.tribbloids.shapesafe.m.shape

import com.tribbloids.shapesafe.BaseSpec
import com.tribbloids.shapesafe.m.arity.Arity
import shapeless.HNil

class EinSumConditionTest extends BaseSpec {

  import shapeless.syntax.singleton.mkSingletonOps

  val ii = "i" ->> Arity(3)

  describe("can append") {

    it("HNil") {

      val out = EinSumCondition.apply(HNil -> ii)

      out.toString.shouldBe("FromLiteral: 3 :: HNil")
    }

    describe("new name to a Record") {

      it("1") {

        val existing = ("a" ->> Arity(4)) :: HNil

        val out = EinSumCondition.apply(existing -> ii)

        out.toString.shouldBe("FromLiteral: 3 :: FromLiteral: 4 :: HNil")
      }

      it("2") {

        val existing = ("a" ->> Arity(4)) :: ("b" ->> Arity(5)) :: HNil

        val out = EinSumCondition.apply(existing -> ii)

        out.toString.shouldBe("FromLiteral: 3 :: FromLiteral: 4 :: FromLiteral: 5 :: HNil")
      }
    }

    describe("old name to a Record that contains identical KV") {

      it(" 1") {

        val existing = ("i" ->> Arity(3)) :: HNil

        val out = EinSumCondition.apply(existing -> ii)

        out.toString.shouldBe("FromLiteral: 3 :: FromLiteral: 4 :: HNil")
      }

      it("2") {

        val existing = ("i" ->> Arity(3)) :: ("j" ->> Arity(4)) :: HNil

        val out = EinSumCondition.apply(existing -> ii)

        out.toString.shouldBe("FromLiteral: 3 :: FromLiteral: 4 :: HNil")
      }
    }

  }

  describe("cannot append if") {}
}
