package org.shapesafe.core.shape.op

import org.shapesafe.BaseSpec
import org.shapesafe.core.arity.LeafArity
import shapeless.HNil

class EinSumConditionSpec extends BaseSpec {

  import shapeless.syntax.singleton.mkSingletonOps
  import shapeless.record._

  val ii = "i" ->> LeafArity(3)

  describe("can append") {

    it("HNil") {

      val out = EinSumCondition.apply(HNil -> ii)

      out.fields.toString.shouldBe("(i,3:Literal) :: HNil")
    }

    describe("new name to a Record") {

      it("1") {

        val existing = ("a" ->> LeafArity(4)) :: HNil

        val out = EinSumCondition.apply(existing -> ii)

        out.fields.toString.shouldBe("(i,3:Literal) :: (a,4:Literal) :: HNil")
      }

      it("2") {

        val existing = ("a" ->> LeafArity(4)) :: ("b" ->> LeafArity(5)) :: HNil

        val out = EinSumCondition.apply(existing -> ii)

        out.fields.toString.shouldBe("(i,3:Literal) :: (a,4:Literal) :: (b,5:Literal) :: HNil")
      }
    }

    describe("old name to a Record that contains identical pair") {

      it(" 1") {

        val existing = ("i" ->> LeafArity(3)) :: HNil

        val out = EinSumCondition.apply(existing -> ii)

        out.fields.toString.shouldBe("(i,3:Literal) :: (i,3:Literal) :: HNil")
      }

      it("2") {

        val existing = ("i" ->> LeafArity(3)) :: ("j" ->> LeafArity(4)) :: HNil

        val out = EinSumCondition.apply(existing -> ii)

        out.fields.toString.shouldBe("(i,3:Literal) :: (i,3:Literal) :: (j,4:Literal) :: HNil")
      }
    }

  }

  describe("cannot append if") {

    describe("old name to a Record that contains identical key but different value") {

      it(" 1") {

        val existing = ("i" ->> LeafArity(4)) :: HNil

        shouldNotCompile(
          """EinSumCondition.apply(existing -> ii)"""
        )

      }

      it("2") {

        val existing = ("i" ->> LeafArity(4)) :: ("j" ->> LeafArity(3)) :: HNil

        shouldNotCompile(
          """val out = EinSumCondition.apply(existing -> ii)"""
        )
      }

      it("3") {

        val existing = ("j" ->> LeafArity(3)) :: ("i" ->> LeafArity(4)) :: HNil

        shouldNotCompile(
          """EinSumCondition.apply(existing -> ii)"""
        )
      }
    }
  }
}
