package com.tribbloids.shapesafe.m.shape

import com.tribbloids.shapesafe.BaseSpec
import com.tribbloids.shapesafe.m.arity.Arity
import shapeless.HNil

class NamedShapeSpec extends BaseSpec {

  import shapeless._
  import record._
  import syntax.singleton._

  describe("einsum") {
    it("1") {

      val s1 = NamedShape(
        (Symbol("x") ->> Arity.FromLiteral(2)) ::
          (Symbol("y") ->> Arity.FromLiteral(3)) ::
          HNil
      )

      val s2 = NamedShape(
        (Symbol("z") ->> Arity.FromLiteral(3)) ::
          (Symbol("w") ->> Arity.FromLiteral(4)) ::
          HNil
      )

      s1.einSum(s2)(Symbol("y"), Symbol("z"))
    }
  }
}
