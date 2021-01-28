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
//      VizType[shape.Static].toString.shouldBe()
    }

    it("2") {

      val shape = Shape ><
        Arity.FromLiteral(2) :<<- "x" ><
        Arity.FromLiteral(3) :<<- "y"

      //    VizType.infer(shape).toString.shouldBe()

      //    VizType[shape.Tail].toString.shouldBe()
//      VizType[shape.Field].toString.shouldBe()
//      VizType[shape.Static].toString.shouldBe()
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

//      VizType.infer(shape.static).toString.shouldBe()

      assert(shape.lookup.apply("x").dimension.number == 2)
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

      assert(shape.lookup.apply("x").dimension.number == 2)
      assert(shape.lookup.apply("z").dimension.number == 4)

      inferShort(shape).shouldBe(
        """shape.Shape.Eye >< (arity.Arity.FromLiteral[Int(2)] :<<- String("x")) >< (arity.Arity.FromLiteral[Int(3)] :<<- axis.Axis.emptyName.type) >< (arity.Arity.FromLiteral[Int(4)] :<<- String("z"))"""
      )
    }
  }

  describe("FromStatic") {

    import shapeless.syntax.singleton.mkSingletonOps

    it("from HNil") {

      val hh = HNil

      val shape = Shape.FromStatic(hh)

      assert(shape == Shape.Eye)
    }

    it("1") {

      val hh = (Arity(3) :<<- "x") ::
        HNil

      val shape = Shape.FromStatic(hh)

      //    VizType.infer(shape).toString.shouldBe()

      assert(shape.static == hh)
    }

    it("2") {

      val hh = (Arity(3) :<<- "x") ::
        (Arity(4) :<<- "y") ::
        HNil

      val shape = Shape.FromStatic(hh)

      //    VizType.infer(shape).toString.shouldBe()

      assert(shape.static == hh)
      assert(shape.static.head.dimension == Arity(3))
    }
  }

  describe("FromRecord") {

    import shapeless.syntax.singleton.mkSingletonOps

    it("from HNil") {

      val hh = HNil

      val shape = Shape.FromName_Dimension(hh)

      assert(shape == Shape.Eye)
    }

    it("1") {

      val hh = ("x" ->> Arity(3)) ::
        HNil

      val shape = Shape.FromName_Dimension(hh)

      //    VizType.infer(shape).toString.shouldBe()

      assert(shape.dimensions.static == hh)
    }

    it("2") {

      val hh = ("x" ->> Arity(3)) ::
        ("y" ->> Arity(4)) ::
        HNil

      val shape = Shape.FromName_Dimension(hh)

      //    VizType.infer(shape).toString.shouldBe()

      assert(shape.dimensions.static == hh)
      assert(shape.static.head.dimension == Arity(3))
    }
  }

  describe("static") {

    it("1") {

      val shape = Shape ><
        Arity.FromLiteral(2) :<<- "x"

      val record = shape.lookup

//      VizType.infer(record).treeString.shouldBe()

      inferShort(record.keys).shouldBe(
        """String("x") :: shapeless.HNil"""
      )

      inferShort(record.values).shouldBe(
        """(arity.Arity.FromLiteral[Int(2)] :<<- String("x")) :: shapeless.HNil"""
      )
    }

    it("2") {

      val shape = Shape ><
        Arity.FromLiteral(2) :<<- "x" ><
        Arity.FromLiteral(3) :<<- "y"

      val record = shape.lookup

      //      VizType.infer(static).treeString.shouldBe()

      inferShort(record.keys).shouldBe(
        """String("y") :: String("x") :: shapeless.HNil"""
      )

      inferShort(record.values).shouldBe(
        """(arity.Arity.FromLiteral[Int(3)] :<<- String("y")) :: (arity.Arity.FromLiteral[Int(2)] :<<- String("x")) :: shapeless.HNil"""
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

    val ab = Names >< "a" >< "b"
    val ij = Names >< "i" >< "j"
    val namesTooMany = Names >< "a" >< "b" >< "c"

    val shapeRenamed = Shape ><
      Arity.FromLiteral(2) :<<- "a" ><
      Arity.FromLiteral(3) :<<- "b"

    it("1") {

//      inferShort(names1).shouldBe()

      val shape2 = shape |<<- ab

//      VizType.infer(shape2).toString.shouldBe()

      VizType.infer(shape2).shouldBe(VizType.infer(shapeRenamed))
    }

    it("2") {

      val shape1 = shape |<<- ij

      val shape2 = shape1 |<<- Names >< "a" >< "b"

//      VizType.infer(shape2).toString.shouldBe()

      VizType.infer(shape2).shouldBe(VizType.infer(shapeRenamed))
    }

    it("should fail if operands are of different dimensions") {

      shouldNotCompile(
        "shape <<- namesTooMany"
      )
    }
  }

  describe("Axes") {

    val shape = Shape ><
      Arity.FromLiteral(2) :<<- "x" ><
      Arity.FromLiteral(3) :<<- "y" ><
      Arity.FromLiteral(4) :<<- "z"

    it("getByIndex") {

      val v = shape.Axes.get(0)

      assert(v == Arity.FromLiteral(4) :<<- "z") // HList is of inverse order
    }

    it("getByName") {

      val v = shape.Axes.get("y")

      assert(v == Arity.FromLiteral(3) :<<- "y")
    }
  }

  describe("concat") {

    it("1") {

      val s1 = Shape ><
        Arity.FromLiteral(2) :<<- "x"
//        Arity.FromLiteral(3) :<<- "y"

      val s2 = Shape ><
        Arity.FromLiteral(2) :<<- "i"
//        Arity.FromLiteral(3) :<<- "j"

      val r = s1 ><>< s2

      inferShort(r).shouldBe(
        """shape.Shape.Eye.type >< (arity.Arity.FromLiteral[Int(2)] :<<- String("x")) >< (arity.Arity.FromLiteral[Int(2)] :<<- String("i"))"""
      )
    }

    it("2") {

      val s1 = Shape ><
        Arity.FromLiteral(2) :<<- "x" ><
        Arity.FromLiteral(3) :<<- "y"

      val s2 = Shape ><
        Arity.FromLiteral(2) :<<- "i" ><
        Arity.FromLiteral(3) :<<- "j"

      val r = s1 concat s2

      inferShort(r).shouldBe(
        "shape.Shape.Eye.type >< (arity.Arity.FromLiteral[Int(2)] :<<- String(\"x\")) >< (arity.Arity.FromLiteral[Int(3)] :<<- String(\"y\"))" +
          " >< (arity.Arity.FromLiteral[Int(2)] :<<- String(\"i\")) >< (arity.Arity.FromLiteral[Int(3)] :<<- String(\"j\"))"
      )
    }
  }

//  describe("einSum") {
//
//    val shape = Shape ><
//      Arity.FromLiteral(2) :<<- "x" ><
//      Arity.FromLiteral(3) :<<- "z"
//
//    it("asOperand") {
//
//      shape.asEinSumOperand(Names >< "i" >< "j")
//    }
//  }
}
