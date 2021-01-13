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

      val shape = Shape ><
        Arity.FromLiteral(2) :<<- "x"

      //    VizType.infer(shape).toString.shouldBe()

      //    VizType[shape.Tail].toString.shouldBe()
//      VizType[shape.Field].toString.shouldBe()
//      VizType[shape.Tail].toString.shouldBe()
//      VizType[shape.tail.type].toString.shouldBe()
      VizType[shape._Record].toString.shouldBe()
    }

    it("2") {

      val shape = Shape ><
        Arity.FromLiteral(2) :<<- "x" ><
        Arity.FromLiteral(3) :<<- "y"

      //    VizType.infer(shape).toString.shouldBe()

      //    VizType[shape.Tail].toString.shouldBe()
//      VizType[shape.Field].toString.shouldBe()
      VizType[shape._Record].toString.shouldBe()
    }
  }

  describe("create") {

    it("named") {

      val shape = Shape ><
        Arity.FromLiteral(2) :<<- "x" cross
        Arity.FromLiteral(3) :<<- "y"

      inferShort(shape).shouldBe(
        """shape.Shape.Eye >< (arity.Arity.FromLiteral[Int(2)] :<<- String("x")) >< (arity.Arity.FromLiteral[Int(3)] :<<- String("y"))"""
      )

      assert(shape.record.apply("x").number == 2)
    }

    it("nameless") {

      val shape = Shape ><
        Arity.FromLiteral(2) cross
        Arity.FromLiteral(3)

      inferShort(shape).shouldBe(
        """shape.Shape.Eye >< (arity.Arity.FromLiteral[Int(2)] :<<- axis.Axis.emptyName.type) >< (arity.Arity.FromLiteral[Int(3)] :<<- axis.Axis.emptyName.type)"""
      )

    }

    it("mixed") {

      val shape = Shape ><
        Arity.FromLiteral(2) :<<- "x" ><
        Arity.FromLiteral(3) cross
        Arity.FromLiteral(4) :<<- "z"

      assert(shape.record.apply("x").number == 2)
      assert(shape.record.apply("z").number == 4)

      inferShort(shape).shouldBe(
        """shape.Shape.Eye >< (arity.Arity.FromLiteral[Int(2)] :<<- String("x")) >< (arity.Arity.FromLiteral[Int(3)] :<<- axis.Axis.emptyName.type) >< (arity.Arity.FromLiteral[Int(4)] :<<- String("z"))"""
      )
    }

    it("ofStatic") {

      // see OfRecord for main unit tests
      import shapeless.syntax.singleton.mkSingletonOps

      val hh = ("x" ->> Arity(3)) ::
        ("y" ->> Arity(4)) ::
        HNil

      val shape = Shape.ofStatic(hh)

      assert(shape.record.head == Arity(3))
    }
  }

  describe("static") {

    it("1") {

      val shape = Shape ><
        Arity.FromLiteral(2) :<<- "x"

      val static = shape.record

//      VizType.infer(static).treeString.shouldBe()

      inferShort(static.keys).shouldBe(
        """String("x") :: shapeless.HNil"""
      )

      inferShort(static.values).shouldBe(
        """arity.Arity.FromLiteral[Int(2)] :: shapeless.HNil"""
      )
    }

    it("2") {

      val shape = Shape ><
        Arity.FromLiteral(2) :<<- "x" ><
        Arity.FromLiteral(3) :<<- "y"

      val static = shape.record

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

    val shape = Shape ><
      Arity.FromLiteral(2) :<<- "x" cross
      Arity.FromLiteral(3) :<<- "y"

    it("1") {

      val nn = shape.names

      assert(nn.head == "y")
    }
  }

  describe("values") {

    val shape = Shape ><
      Arity.FromLiteral(2) :<<- "x" ><
      Arity.FromLiteral(3) :<<- "y"

    it("1") {

//      val _vv = shape.valuesFactory //.apply(shape.static)
//      VizType.infer(_vv).toString.shouldBe()

      val vv = shape.dimensions

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

    val shape = Shape ><
      Arity.FromLiteral(2) :<<- "x" ><
      Arity.FromLiteral(3) :<<- "y"

    val names1 = Names >< "a" >< "b"
    val names2 = Names >< "a" >< "b" >< "c"

    val shapeRenamed = Shape ><
      Arity.FromLiteral(2) :<<- "a" ><
      Arity.FromLiteral(3) :<<- "b"

    it("1") {

//      inferShort(names1).shouldBe()

      val shape2 = shape |<<- names1

      VizType.infer(shape2).shouldBe(VizType.infer(shapeRenamed))

    }

    it("2") {

      val shape2 = shape |<<- Names >< "a" >< "b"

      VizType.infer(shape2).shouldBe(VizType.infer(shapeRenamed))
    }

    it("should fail if operands are of different dimensions") {

      shouldNotCompile(
        "shape <<- names2"
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
