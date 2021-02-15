package org.shapesafe.core.shape.op

import org.shapesafe.BaseSpec
import org.shapesafe.core.arity.Arity
import shapeless.HNil

class EinSumIndexedSpec extends BaseSpec {

  import shapeless.syntax.singleton.mkSingletonOps

  describe("can create") {

    it("singleton") {

//      val eye = EinSumOperand.Eye

      val op = EinSumIndexed ><
        ("i" ->> Arity(3))
    }

    it("if names has no duplicate") {

      val op = EinSumIndexed ><
        ("i" ->> Arity(3)) ><
        ("j" ->> Arity(4))

      val op2 = op ><
        ("k" ->> Arity(5))
    }

    it("if name-dimension is a map") {

      val op = EinSumIndexed ><
        ("i" ->> Arity(3)) ><
        ("j" ->> Arity(4))

//      VizType[op.Static].toString.shouldBe()
//      val v1 = EinSumCondition.apply(op.static -> ("j" ->> Arity(4)))
//      VizType.infer(v1).toString.shouldBe()

      val op1 = op ><
        ("j" ->> Arity(4))

      val op2 = op1 ><
        ("i" ->> Arity(3))
    }
  }

  describe("CANNOT create") {

    it("if name-dimension mapping is NOT a map") {

      val op = EinSumIndexed ><
        ("i" ->> Arity(3)) ><
        ("j" ->> Arity(4))

      shouldNotCompile(
        """op >< ("j" ->> Arity(3))"""
      )
    }
  }

  describe("can convert FromStatic") {

    it("if HNil") {
      val record = HNil

      val op = EinSumIndexed.FromStatic(record)
    }

    it("if singleton") {
      val record = ("j" ->> Arity(4)) :: HNil

      val vv = "j" ->> Arity(4)

      val op = EinSumIndexed.FromStatic(record)

//      val op = EinSumOperand.FromStatic.recursive[HNil, EinSumOperand.Eye, vv.type].apply(record)
    }

    it("if name-dimension is a map") {
      val record = ("i" ->> Arity(3)) ::
        ("j" ->> Arity(4)) ::
        ("i" ->> Arity(3)) ::
        ("j" ->> Arity(4)) :: HNil

      val op = EinSumIndexed.FromStatic(record)
    }
  }

  describe("CANNOT convert FromStatic") {

    it("if name-dimension is NOT a map") {
      val record = ("i" ->> Arity(3)) ::
        ("j" ->> Arity(4)) ::
        ("i" ->> Arity(4)) :: HNil

      shouldNotCompile(
        """EinSumOperand.FromStatic.apply(record)"""
      )
    }
  }
}
