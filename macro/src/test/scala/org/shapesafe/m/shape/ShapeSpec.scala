package org.shapesafe.m.shape

import com.tribbloids.graph.commons.util.ScalaReflection
import com.tribbloids.graph.commons.util.viz.VizType
import org.shapesafe.BaseSpec
import org.shapesafe.m.arity.Arity
import org.shapesafe.m.shape.Shape.FromIndex
import shapeless.HNil

class ShapeSpec extends BaseSpec {

  import shapeless.record._

  ignore("spike") {

    it("1") {

      val shape = Shape ><
        Arity.Literal(2) :<<- "x"

      //    VizType.infer(shape).toString.shouldBe()

      //    VizType[shape.Tail].toString.shouldBe()
//      VizType[shape.Field].toString.shouldBe()
//      VizType[shape.Tail].toString.shouldBe()
//      VizType[shape.tail.type].toString.shouldBe()
//      VizType[shape.Static].toString.shouldBe()
    }

    it("2") {

      val shape = Shape ><
        Arity.Literal(2) :<<- "x" ><
        Arity.Literal(3) :<<- "y"

      //    VizType.infer(shape).toString.shouldBe()

      //    VizType[shape.Tail].toString.shouldBe()
//      VizType[shape.Field].toString.shouldBe()
//      VizType[shape.Static].toString.shouldBe()
    }
  }

  describe("create") {

    it("named") {

      val shape = Shape ><
        Arity.Literal(2) :<<- "x" cross
        Arity.Literal(3) :<<- "y"

      typeInferShort(shape).shouldBe(
        """
          |Shape.Eye >< (Arity.Literal[Int(2)] :<<- String("x")) >< (Arity.Literal[Int(3)] :<<- String("y"))
          |""".stripMargin
      )

//      VizType.infer(shape.static).toString.shouldBe()

      assert(shape.namedIndex.apply("x").dimension.number == 2)
    }

    it("nameless") {

      val shape = Shape ><
        Arity.Literal(2) cross
        Arity.Literal(3)

      typeInferShort(shape).shouldBe(
        """
          |Shape.Eye >< (Arity.Literal[Int(2)] :<<- Axis.emptyName.type) >< (Arity.Literal[Int(3)] :<<- Axis.emptyName.type)
          |""".stripMargin
      )

    }

    it("mixed") {

      val shape = Shape ><
        Arity.Literal(2) :<<- "x" ><
        Arity.Literal(3) cross
        Arity.Literal(4) :<<- "z"

      assert(shape.namedIndex.apply("x").dimension.number == 2)
      assert(shape.namedIndex.apply("z").dimension.number == 4)

      typeInferShort(shape).shouldBe(
        """
          |Shape.Eye >< (Arity.Literal[Int(2)] :<<- String("x")) >< (Arity.Literal[Int(3)] :<<- Axis.emptyName.type) >< (Arity.Literal[Int(4)] :<<- String("z"))
          |""".stripMargin
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

  describe(FromIndex.getClass.getSimpleName) {

    import shapeless.syntax.singleton.mkSingletonOps

    it("from HNil") {

      val hh = HNil

      val shape = Shape.FromIndex(hh)

      assert(shape == Shape.Eye)
    }

    it("1") {

      val hh = ("x" ->> Arity(3)) ::
        HNil

      val shape = Shape.FromIndex(hh)

      //    VizType.infer(shape).toString.shouldBe()

      assert(shape.dimensions.static == hh)
    }

    it("2") {

      val hh = ("x" ->> Arity(3)) ::
        ("y" ->> Arity(4)) ::
        HNil

      val shape = Shape.FromIndex(hh)

      //    VizType.infer(shape).toString.shouldBe()

      assert(shape.dimensions.static == hh)
      assert(shape.static.head.dimension == Arity(3))
    }
  }

  describe("FromNats") {

    it("1") {
      val ss = Shape(4)

      ss.dimensions.static.head.internal.requireEqual(4)
      ss.dimensions.static.last.internal.requireEqual(4)
    }

    it("2") {
      val ss = Shape(4, 3, 2)

      ss.dimensions.static.head.internal.requireEqual(2)
      ss.dimensions.static.last.internal.requireEqual(4)
    }
  }

  describe("static") {

    it("1") {

      val shape = Shape ><
        Arity.Literal(2) :<<- "x"

      val record = shape.namedIndex

//      VizType.infer(record).treeString.shouldBe()

      typeInferShort(record.keys).shouldBe(
        """
          |String("x") :: HNil""".stripMargin
      )

      typeInferShort(record.values).shouldBe(
        """
          |(Arity.Literal[Int(2)] :<<- String("x")) :: HNil""".stripMargin
      )
    }

    it("2") {

      val shape = Shape ><
        Arity.Literal(2) :<<- "x" ><
        Arity.Literal(3) :<<- "y"

      val record = shape.namedIndex

      //      VizType.infer(static).treeString.shouldBe()

      typeInferShort(record.keys).shouldBe(
        """
          |String("y") :: String("x") :: HNil""".stripMargin
      )

      typeInferShort(record.values).shouldBe(
        """
          |(Arity.Literal[Int(3)] :<<- String("y")) :: (Arity.Literal[Int(2)] :<<- String("x")) :: HNil
          |""".stripMargin
      )
    }
  }

  describe("names") {

    val shape = Shape ><
      Arity.Literal(2) :<<- "x" cross
      Arity.Literal(3) :<<- "y"

    it("1") {

      val nn = shape.names

      assert(nn.head == "y")
    }
  }

  describe("values") {

    val shape = Shape ><
      Arity.Literal(2) :<<- "x" ><
      Arity.Literal(3) :<<- "y"

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
      Arity.Literal(2) :<<- "x" ><
      Arity.Literal(3) :<<- "y"

    val ab = Names >< "a" >< "b"
    val ij = Names >< "i" >< "j"
    val namesTooMany = Names >< "a" >< "b" >< "c"

    val shapeRenamed = Shape ><
      Arity.Literal(2) :<<- "a" ><
      Arity.Literal(3) :<<- "b"

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
      Arity.Literal(2) :<<- "x" ><
      Arity.Literal(3) :<<- "y" ><
      Arity.Literal(4) :<<- "z"

    it("getByIndex") {

      val v = shape.Axes.get(0)

      assert(v == Arity.Literal(4) :<<- "z") // HList is of inverse order
    }

    it("getByName") {

      val v = shape.Axes.get("y")

      assert(v == Arity.Literal(3) :<<- "y")
    }
  }

  describe("concat") {

    it("1") {

      val s1 = Shape ><
        Arity.Literal(2) :<<- "x"
//        Arity.FromLiteral(3) :<<- "y"

      val s2 = Shape ><
        Arity.Literal(2) :<<- "i"
//        Arity.FromLiteral(3) :<<- "j"

      val r = s1 >< s2

      typeInferShort(r).shouldBe(
        """
          |Shape.eye.type >< (Arity.Literal[Int(2)] :<<- String("x")) >< (Arity.Literal[Int(2)] :<<- String("i"))""".stripMargin
      )
    }

    it("2") {

      val s1 = Shape ><
        Arity.Literal(2) :<<- "x" ><
        Arity.Literal(3) :<<- "y"

      val s2 = Shape ><
        Arity.Literal(2) :<<- "i" ><
        Arity.Literal(3) :<<- "j"

      val r = s1 concat s2

      typeInferShort(r).shouldBe(
        """
          |
          |Shape.eye.type >< (Arity.Literal[Int(2)] :<<- String("x")) >< (Arity.Literal[Int(3)] :<<- String("y")) ><
          | (Arity.Literal[Int(2)] :<<- String("i")) >< (Arity.Literal[Int(3)] :<<- String("j"))
          |""".stripMargin.split('\n').mkString
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
