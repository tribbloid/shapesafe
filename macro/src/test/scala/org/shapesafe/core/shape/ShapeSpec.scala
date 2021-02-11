package org.shapesafe.core.shape

import com.tribbloids.graph.commons.util.viz.VizType
import org.shapesafe.BaseSpec
import org.shapesafe.core.arity.Leaf
import org.shapesafe.core.shape.Shape.{FromIndex, FromNats, FromStatic}
import shapeless.HNil

class ShapeSpec extends BaseSpec {

  import shapeless.record._

  ignore("spike") {

    it("1") {

      val shape = Shape ><
        Leaf.Literal(2) :<<- "x"

      //    VizType.infer(shape).toString.shouldBe()

      //    VizType[shape.Tail].toString.shouldBe()
//      VizType[shape.Field].toString.shouldBe()
//      VizType[shape.Tail].toString.shouldBe()
//      VizType[shape.tail.type].toString.shouldBe()
//      VizType[shape.Static].toString.shouldBe()
    }

    it("2") {

      val shape = Shape ><
        Leaf.Literal(2) :<<- "x" ><
        Leaf.Literal(3) :<<- "y"

      //    VizType.infer(shape).toString.shouldBe()

      //    VizType[shape.Tail].toString.shouldBe()
//      VizType[shape.Field].toString.shouldBe()
//      VizType[shape.Static].toString.shouldBe()
    }
  }

  describe("create") {

    it("named") {

      val shape = Shape ><
        Leaf.Literal(2) :<<- "x" cross
        Leaf.Literal(3) :<<- "y"

      typeInferShort(shape).shouldBe(
        """
          |Shape.Eye >< (Leaf.Literal[Int(2)] :<<- String("x")) >< (Leaf.Literal[Int(3)] :<<- String("y"))
          |""".stripMargin
      )

//      VizType.infer(shape.static).toString.shouldBe()

      assert(shape.indexToFields.apply("x").dimension.number == 2)
    }

    it("nameless") {

      val shape = Shape ><
        Leaf.Literal(2) cross
        Leaf.Literal(3)

      typeInferShort(shape).shouldBe(
        """
          |Shape.Eye >< (Leaf.Literal[Int(2)] :<<- Axis.emptyName.type) >< (Leaf.Literal[Int(3)] :<<- Axis.emptyName.type)
          |""".stripMargin
      )

    }

    it("mixed") {

      val shape = Shape ><
        Leaf.Literal(2) :<<- "x" ><
        Leaf.Literal(3) cross
        Leaf.Literal(4) :<<- "z"

      assert(shape.indexToFields.apply("x").dimension.number == 2)
      assert(shape.indexToFields.apply("z").dimension.number == 4)

      typeInferShort(shape).shouldBe(
        """
          |Shape.Eye >< (Leaf.Literal[Int(2)] :<<- String("x")) >< (Leaf.Literal[Int(3)] :<<- Axis.emptyName.type) >< (Leaf.Literal[Int(4)] :<<- String("z"))
          |""".stripMargin
      )
    }
  }

  describe(FromStatic.getClass.getSimpleName) {

    it("from HNil") {

      val hh = HNil

      val shape = Shape.FromStatic(hh)

      assert(shape == Shape.Eye)
    }

    it("1") {

      val hh = (Leaf(3) :<<- "x") ::
        HNil

      val shape = Shape.FromStatic(hh)

      //    VizType.infer(shape).toString.shouldBe()

      assert(shape.static == hh)
    }

    it("2") {

      val hh = (Leaf(3) :<<- "x") ::
        (Leaf(4) :<<- "y") ::
        HNil

      val shape = Shape.FromStatic(hh)

      //    VizType.infer(shape).toString.shouldBe()

      assert(shape.static == hh)
      assert(shape.static.head.dimension == Leaf(3))
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

      val hh = ("x" ->> Leaf(3)) ::
        HNil

      val shape = Shape.FromIndex(hh)

      //    VizType.infer(shape).toString.shouldBe()

      assert(shape.dimensions.static == hh)
    }

    it("2") {

      val hh = ("x" ->> Leaf(3)) ::
        ("y" ->> Leaf(4)) ::
        HNil

      val shape = Shape.FromIndex(hh)

      //    VizType.infer(shape).toString.shouldBe()

      assert(shape.dimensions.static == hh)
      assert(shape.static.head.dimension == Leaf(3))
    }
  }

  describe(FromNats.getClass.getSimpleName) {

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

  describe("index") {

    it("1") {

      val shape = Shape ><
        Leaf.Literal(2) :<<- "x"

      val record = shape.index

      //      VizType.infer(record).treeString.shouldBe()

      typeInferShort(record.keys).shouldBe(
        """
          |String("x") :: HNil""".stripMargin
      )

      typeInferShort(record.values).shouldBe(
        """
          |Leaf.Literal[Int(2)] :: HNil""".stripMargin
      )

      assert(record.get("x") == Leaf.Literal(2))
    }

    it("2") {

      val shape = Shape ><
        Leaf.Literal(2) :<<- "x" ><
        Leaf.Literal(3) :<<- "y"

      val record = shape.index

      //      VizType.infer(static).treeString.shouldBe()

      typeInferShort(record.keys).shouldBe(
        """
          |String("y") :: String("x") :: HNil""".stripMargin
      )

      typeInferShort(record.values).shouldBe(
        """
          |Leaf.Literal[Int(3)] :: Leaf.Literal[Int(2)] :: HNil
          |""".stripMargin
      )

      assert(record.get("x") == Leaf.Literal(2))
    }
  }

  describe("indexToFields") {

    it("1") {

      val shape = Shape ><
        Leaf.Literal(2) :<<- "x"

      val record = shape.indexToFields

//      VizType.infer(record).treeString.shouldBe()

      typeInferShort(record.keys).shouldBe(
        """
          |String("x") :: HNil""".stripMargin
      )

      typeInferShort(record.values).shouldBe(
        """
          |(Leaf.Literal[Int(2)] :<<- String("x")) :: HNil""".stripMargin
      )

      assert(record.get("x") == Leaf.Literal(2) :<<- "x")
    }

    it("2") {

      val shape = Shape ><
        Leaf.Literal(2) :<<- "x" ><
        Leaf.Literal(3) :<<- "y"

      val record = shape.indexToFields

      //      VizType.infer(static).treeString.shouldBe()

      typeInferShort(record.keys).shouldBe(
        """
          |String("y") :: String("x") :: HNil""".stripMargin
      )

      typeInferShort(record.values).shouldBe(
        """
          |(Leaf.Literal[Int(3)] :<<- String("y")) :: (Leaf.Literal[Int(2)] :<<- String("x")) :: HNil
          |""".stripMargin
      )

      assert(record.get("x") == Leaf.Literal(2) :<<- "x")
    }
  }

  describe("names") {

    val shape = Shape ><
      Leaf.Literal(2) :<<- "x" cross
      Leaf.Literal(3) :<<- "y"

    it("1") {

      val nn = shape.names

      assert(nn.head == "y")
    }
  }

  describe("values") {

    val shape = Shape ><
      Leaf.Literal(2) :<<- "x" ><
      Leaf.Literal(3) :<<- "y"

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

      assert(vv.head == Leaf(3))

//      vv.head.toString.shouldBe("FromLiteral: 3")

//      val vv2 = ss.values(_vv2)
//      VizType.infer(vv2).toString.shouldBe()

    }
  }

  describe("withNames") {

    val shape = Shape ><
      Leaf.Literal(2) :<<- "x" ><
      Leaf.Literal(3) :<<- "y"

    val ab = Names >< "a" >< "b"
    val ij = Names >< "i" >< "j"
    val namesTooMany = Names >< "a" >< "b" >< "c"

    val shapeRenamed = Shape ><
      Leaf.Literal(2) :<<- "a" ><
      Leaf.Literal(3) :<<- "b"

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
      Leaf.Literal(2) :<<- "x" ><
      Leaf.Literal(3) :<<- "y" ><
      Leaf.Literal(4) :<<- "z"

    it("getByIndex") {

      val v = shape.Axes.get(0)

      assert(v == Leaf.Literal(4) :<<- "z") // HList is of inverse order
    }

    it("getByName") {

      val v = shape.Axes.get("y")

      assert(v == Leaf.Literal(3) :<<- "y")
    }
  }

  describe("concat") {

    it("1") {

      val s1 = Shape ><
        Leaf.Literal(2) :<<- "x"
//        Arity.FromLiteral(3) :<<- "y"

      val s2 = Shape ><
        Leaf.Literal(2) :<<- "i"
//        Arity.FromLiteral(3) :<<- "j"

      val r = s1 >< s2

      typeInferShort(r).shouldBe(
        """
          |Shape.eye.type >< (Leaf.Literal[Int(2)] :<<- String("x")) >< (Leaf.Literal[Int(2)] :<<- String("i"))""".stripMargin
      )
    }

    it("2") {

      val s1 = Shape ><
        Leaf.Literal(2) :<<- "x" ><
        Leaf.Literal(3) :<<- "y"

      val s2 = Shape ><
        Leaf.Literal(2) :<<- "i" ><
        Leaf.Literal(3) :<<- "j"

      val r = s1 concat s2

      typeInferShort(r).shouldBe(
        """
          |
          |Shape.eye.type >< (Leaf.Literal[Int(2)] :<<- String("x")) >< (Leaf.Literal[Int(3)] :<<- String("y")) ><
          | (Leaf.Literal[Int(2)] :<<- String("i")) >< (Leaf.Literal[Int(3)] :<<- String("j"))
          |""".stripMargin.split('\n').mkString
      )
    }
  }

  describe("transpose") {

    it("1") {

      val shape = Shape ><
        Leaf(1) :<<- "a" ><
        Leaf(2) :<<- "b" ><
        Leaf(3) :<<- "c"

      val r = shape.transpose(Names >< "c")

      val cc = r.getClass

      r.toString.shouldBe(
        """
          |Eye ><
          |  3:Literal :<<- c
          |""".stripMargin
      )
    }

    it("... alternative syntax") {
      import Names.Syntax._

      val shape = Shape(1, 2, 3) |<<- ("a" >< "b" >< "c")

      val r = shape.transpose("c" >< "b" >< "a")

      r.toString.shouldBe(
        """
          |Eye ><
          |  3:Derived :<<- c ><
          |  2:Derived :<<- b ><
          |  1:Derived :<<- a
          |""".stripMargin
      )
    }
  }
}
