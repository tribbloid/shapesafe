package com.tribbloids.shapesafe.m.shape

import com.tribbloids.graph.commons.util.ScalaReflection
import com.tribbloids.graph.commons.util.viz.VizType
import com.tribbloids.shapesafe.BaseSpec
import com.tribbloids.shapesafe.m.arity.Arity
import shapeless.HNil

class ShapeSpec extends BaseSpec {

  import shapeless.record._

  def inferShort[T: ScalaReflection.TypeTag](v: T): String = {

    VizType
      .infer(v)
      .typeStr
      .replaceAllLiterally("com.tribbloids.shapesafe.m.", "")
  }

  ignore("spike") {

    it("1") {

      val shape = Shape |>
        Arity.FromLiteral(2) <<- "x"

      //    VizType.infer(shape).toString.shouldBe()

      //    VizType[shape.Tail].toString.shouldBe()
//      VizType[shape.Field].toString.shouldBe()
//      VizType[shape.Tail].toString.shouldBe()
//      VizType[shape.tail.type].toString.shouldBe()
      VizType[shape.Static].toString.shouldBe()
    }

    it("2") {

      val shape = Shape |>
        Arity.FromLiteral(2) <<- "x" |
        Arity.FromLiteral(3) <<- "y"

      //    VizType.infer(shape).toString.shouldBe()

      //    VizType[shape.Tail].toString.shouldBe()
//      VizType[shape.Field].toString.shouldBe()
      VizType[shape.Static].toString.shouldBe()
    }
  }

  describe("create") {

    it("named") {

      val shape = Shape |>
        Arity.FromLiteral(2) <<- "x" cross
        Arity.FromLiteral(3) <<- "y"

      inferShort(shape).shouldBe(
        """shape.Shape.SNil | (arity.Arity.FromLiteral[Int(2)] <<- String("x")) | (arity.Arity.FromLiteral[Int(3)] <<- String("y"))"""
      )

      assert(shape.static.apply("x").number == 2)
    }

    it("nameless") {

      val shape = Shape |>
        Arity.FromLiteral(2) cross
        Arity.FromLiteral(3)

      inferShort(shape).shouldBe(
        """shape.Shape.SNil | (arity.Arity.FromLiteral[Int(2)] <<- arity.Dim.emptyName.type) | (arity.Arity.FromLiteral[Int(3)] <<- arity.Dim.emptyName.type)"""
      )

    }

    it("mixed") {

      val shape = Shape |>
        Arity.FromLiteral(2) <<- "x" |
        Arity.FromLiteral(3) cross
        Arity.FromLiteral(4) <<- "z"

      assert(shape.static.apply("x").number == 2)
      assert(shape.static.apply("z").number == 4)

      inferShort(shape).shouldBe(
        """shape.Shape.SNil | (arity.Arity.FromLiteral[Int(2)] <<- String("x")) | (arity.Arity.FromLiteral[Int(3)] <<- arity.Dim.emptyName.type) | (arity.Arity.FromLiteral[Int(4)] <<- String("z"))"""
      )
    }

    it("ofStatic") {

      // see OfRecord for main unit tests
      import shapeless.syntax.singleton.mkSingletonOps

      val hh = ("x" ->> Arity(3)) ::
        ("y" ->> Arity(4)) ::
        HNil

      val shape = Shape.ofStatic(hh)

      shape.static.head.toString.shouldBe()
    }
  }

  describe("static") {

    it("1") {

      val shape = Shape |>
        Arity.FromLiteral(2) <<- "x"

      val static = shape.static

//      VizType.infer(static).treeString.shouldBe()

      inferShort(static.keys).shouldBe(
        """String("x") :: shapeless.HNil"""
      )

      inferShort(static.values).shouldBe(
        """arity.Arity.FromLiteral[Int(2)] :: shapeless.HNil"""
      )
    }

    it("2") {

      val shape = Shape |>
        Arity.FromLiteral(2) <<- "x" |
        Arity.FromLiteral(3) <<- "y"

      val static = shape.static

      //      VizType.infer(static).treeString.shouldBe()

      inferShort(static.keys).shouldBe(
        """String("y") :: String("x") :: shapeless.HNil"""
      )

      inferShort(static.values).shouldBe(
        """arity.Arity.FromLiteral[Int(3)] :: arity.Arity.FromLiteral[Int(2)] :: shapeless.HNil"""
      )
    }
  }

  describe("names") {

    val shape = Shape |>
      Arity.FromLiteral(2) <<- "x" cross
      Arity.FromLiteral(3) <<- "y"

    it("1") {

      val nn = shape.names

      assert(nn.head == "y")
    }
  }

  describe("values") {

    val shape = Shape |>
      Arity.FromLiteral(2) <<- "x" |
      Arity.FromLiteral(3) <<- "y"

    it("1") {

//      val _vv = shape.valuesFactory //.apply(shape.static)
//      VizType.infer(_vv).toString.shouldBe()

      val vv = shape.values

//      VizType
//        .infer(vv)
//        .toString
//        .shouldBe(
//          VizType.infer(vvGT).toString
//        )

      assert(vv.head == Arity(3))

//      vv.head.toString.shouldBe("FromLiteral: 3")

//      val vv2 = ss.values(_vv2)
//      VizType.infer(vv2).toString.shouldBe()

    }
  }

  describe("withNames") {

//    it("spike") {
//
//      val shape = Shape |>
//        Arity.FromLiteral(2) <<- "x" |
//        Arity.FromLiteral(3) <<- "y"
//
//      val newNames = NamesView |> "a" | "b"
//
//      VizType.infer(newNames).treeString.shouldBe()
//
//      val vv = shape.withNamesProto(newNames)
//
//      VizType.infer(vv).treeString.shouldBe()
//
//    }

    val shape = Shape |>
      Arity.FromLiteral(2) <<- "x" |
      Arity.FromLiteral(3) <<- "y"

    it("1") {

      val newNames = NamesView |> "a" | "b"

      inferShort(newNames).shouldBe(
        // TODO: hard to read comparing to Shape
        """shape.NamesView[String("b") :: String("a") :: shapeless.HNil]"""
      )

      val shape2 = shape <<- newNames

      inferShort(shape2).shouldBe(
        """shape.Shape.SNil | (shape.tail.head.Value <<- String("a")) | (shape.head.Value <<- String("b"))"""
      )
    }

    it("2") {

      val newNames = NamesView |> "a" | "b" | "c"

      shouldNotCompile(
        "shape <<- newNames"
      )
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
