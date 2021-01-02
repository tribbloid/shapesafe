package com.tribbloids.shapesafe.m.shape

import com.tribbloids.shapesafe.BaseSpec
import com.tribbloids.shapesafe.m.arity.Arity

class NamedShapeSpec extends BaseSpec {

  describe("create") {

    it("named") {

      val shape = Shape ~
        (Arity.FromLiteral(2) <<- "x") |
        (Arity.FromLiteral(3) <<- "y")
    }

    it("nameless") {

      val shape = Shape ~
        Arity.FromLiteral(2) |
        Arity.FromLiteral(3)
    }

    it("mixed") {

      val shape = Shape ~
        (Arity.FromLiteral(2) <<- "x") |
        (Arity.FromLiteral(3)) |
        (Arity.FromLiteral(2) <<- "z")
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
