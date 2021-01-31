package com.tribbloids.shapesafe.m.shape.op

import com.tribbloids.shapesafe.BaseSpec
import com.tribbloids.shapesafe.m.arity.Arity
import com.tribbloids.shapesafe.m.shape.Shape

class EinSumSpec extends BaseSpec {

  describe("ops") {

    it("eye") {

      val s1 = Shape.Eye

      val ops = Shape.einSumOps(s1)
    }

    it("1") {

      val s1 = Shape ><
        (Arity(1) :<<- "x")

      val ops = Shape.einSumOps(s1)
    }

    it("2") {

      val s1 = Shape ><
        (Arity(1) :<<- "x") ><
        (Arity(2) :<<- "y") ><
        (Arity(1) :<<- "x")

      val ops = Shape.einSumOps(s1)
    }

  }

  describe("can einSum") {

    it("") {

      val s1 = Shape.Eye

      val r = s1.einSum(
        Shape ><
          (Arity(1) :<<- "i")
      )
    }

    it("2") {
      val s1 = Shape ><
        (Arity(1) :<<- "x") ><
        Arity(2) :<<- "y"

      val r = s1.einSum(
        Shape ><
          (Arity(1) :<<- "i") ><
          Arity(2) :<<- "j"
      )
    }

//    it("2") {
//      val s1 = Shape ><
//        (Arity(1) :<<- "x") ><
//        Arity(2) :<<- "y"
//
//      val r = s1.einSum(
//        Shape ><
//          (Arity(1) :<<- "x") ><
//          Arity(2) :<<- "y"
//      )
//    }

  }

  describe("cannot einsum") {}
}
