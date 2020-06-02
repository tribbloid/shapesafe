package edu.umontreal.kotlingrad.shapesafe

import shapeless.HNil
import shapeless.::

class ConstraintSpec extends BaseSpec {

  describe("prove") {

    it("base") {
      val proof = implicitly[Constraint.OfElementType[HNil, Int]]

      assert(proof.ttg.tpe.toString == "Int")
    }

    it("1 element") {

      val proof = implicitly[Constraint.OfElementType[Int :: HNil, Int]]

      assert(proof.ttg.tpe.toString == "Int")
    }

    it("2 elements") {

      val proof = implicitly[Constraint.OfElementType[Int :: Int :: HNil, Int]]

      assert(proof.ttg.tpe.toString == "Int")
    }

    it("5 elements") {

      val proof = implicitly[Constraint.OfElementType[Int :: Int :: Int :: Int :: Int :: HNil, Int]]

      assert(proof.ttg.tpe.toString == "Int")
    }

    it("if members are of different subtypes") {

      import java.lang

      val proof =
        implicitly[
          Constraint.OfElementType[lang.Integer :: lang.Double :: lang.Float :: lang.Long :: HNil, lang.Number]]

      assert(proof.ttg.tpe.toString == "Number")
    }
  }

  it("can NOT prove if a member is not a subtype") {

    // doesn't compile
    // TODO: use shapeless to assert that
    //    val proof = implicitly[Constraint.OfElementType[Int :: Int :: Int :: Double :: Int :: HNil, Int]]
  }
}
