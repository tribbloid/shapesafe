package com.tribbloids.shapesafe.m.shape

import com.tribbloids.shapesafe.BaseSpec
import com.tribbloids.shapesafe.m.arity.{Arity, Expression}

class NamedShapeSpec extends BaseSpec {

  import shapeless.record._

  describe("create") {

    it("named") {

      val shape = Shape |>
        Arity.FromLiteral(2) <<- "x" cross
        Arity.FromLiteral(3) <<- "y"

      assert(shape.dimensions.apply("x").number == 2)
    }

    it("nameless") {

      val shape = Shape |>
        Arity.FromLiteral(2) cross
        Arity.FromLiteral(3)
    }

    it("mixed") {

      val shape = Shape |>
        Arity.FromLiteral(2) <<- "x" |
        Arity.FromLiteral(3) cross
        Arity.FromLiteral(4) <<- "z"

      assert(shape.dimensions.apply("x").number == 2)
      assert(shape.dimensions.apply("z").number == 4)
    }
  }

//  describe("einsum") {
//    it("1") {
//
//      val s1 = NamedShape(
//        ("x" -> Arity.FromLiteral(2)) ::
//          ("y" -> Arity.FromLiteral(3)) ::
//          HNil
//      )
//
//      val s2 = NamedShape(
//        ("z" -> Arity.FromLiteral(3)) ::
//          ("w" -> Arity.FromLiteral(4)) ::
//          HNil
//      )
//
//      s1.einSum(s2)("y", "z")
//    }
//  }
}
