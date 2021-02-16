package org.shapesafe.core.shape

import com.tribbloids.graph.commons.util.viz.VizType
import org.shapesafe.BaseSpec
import org.shapesafe.core.arity.{Arity, LeafArity}
import org.shapesafe.core.shape.Shape.{FromNats, FromRecord, FromStatic}
import shapeless.HNil

class ShapeSpec extends BaseSpec {

  import shapeless.record._

  ignore("spike") {

    it("1") {

      val shape = Shape ><
        LeafArity.Literal(2) :<<- "x"

      //    VizType.infer(shape).toString.shouldBe()

      //    VizType[shape.Tail].toString.shouldBe()
//      VizType[shape.Field].toString.shouldBe()
//      VizType[shape.Tail].toString.shouldBe()
//      VizType[shape.tail.type].toString.shouldBe()
//      VizType[shape.Static].toString.shouldBe()
    }

    it("2") {

      val shape = Shape ><
        LeafArity.Literal(2) :<<- "x" ><
        LeafArity.Literal(3) :<<- "y"

      //    VizType.infer(shape).toString.shouldBe()

      //    VizType[shape.Tail].toString.shouldBe()
//      VizType[shape.Field].toString.shouldBe()
//      VizType[shape.Static].toString.shouldBe()
    }
  }

  describe("create") {

    it("named") {

      val shape = Shape ><
        LeafArity.Literal(2) :<<- "x" cross
        LeafArity.Literal(3) :<<- "y"

      typeInferShort(shape).shouldBe(
        """
          |Shape.Eye >< (LeafArity.Literal[Int(2)] :<<- String("x")) >< (LeafArity.Literal[Int(3)] :<<- String("y"))
          |""".stripMargin
      )

//      VizType.infer(shape.static).toString.shouldBe()

      assert(shape.record.apply("x").runtime == 2)
    }

    it("nameless") {

      val shape = Shape ><
        LeafArity.Literal(2) cross
        LeafArity.Literal(3)

      typeInferShort(shape).shouldBe(
        """
          |Shape.Eye >< (LeafArity.Literal[Int(2)] :<<- Axis.emptyName.type) >< (LeafArity.Literal[Int(3)] :<<- Axis.emptyName.type)
          |""".stripMargin
      )

    }

    it("mixed") {

      val shape = Shape ><
        LeafArity.Literal(2) :<<- "x" ><
        LeafArity.Literal(3) cross
        LeafArity.Literal(4) :<<- "z"

      assert(shape.record.apply("x").runtime == 2)
      assert(shape.record.apply("z").runtime == 4)

      typeInferShort(shape).shouldBe(
        """
          |Shape.Eye >< (LeafArity.Literal[Int(2)] :<<- String("x")) >< (LeafArity.Literal[Int(3)] :<<- Axis.emptyName.type) >< (LeafArity.Literal[Int(4)] :<<- String("z"))
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

  describe(FromRecord.getClass.getSimpleName) {

    import shapeless.syntax.singleton.mkSingletonOps

    it("from HNil") {

      val hh = HNil

      val shape = Shape.FromRecord(hh)

      assert(shape == Shape.Eye)
    }

    it("1") {

      val hh = ("x" ->> Arity(3)) ::
        HNil

      val shape = Shape.FromRecord(hh)

      //    VizType.infer(shape).toString.shouldBe()

      assert(shape.dimensions.static == hh)
    }

    it("2") {

      val hh = ("x" ->> Arity(3)) ::
        ("y" ->> Arity(4)) ::
        HNil

      val shape = Shape.FromRecord(hh)

      //    VizType.infer(shape).toString.shouldBe()

      assert(shape.dimensions.static == hh)
      assert(shape.static.head.dimension == Arity(3))
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
        LeafArity.Literal(2) :<<- "x"

      val record = shape.record

      //      VizType.infer(record).treeString.shouldBe()

      typeInferShort(record.keys).shouldBe(
        """
          |String("x") :: HNil""".stripMargin
      )

      typeInferShort(record.values).shouldBe(
        """
          |LeafArity.Literal[Int(2)] :: HNil""".stripMargin
      )

      assert(record.get("x") == LeafArity.Literal(2))
    }

    it("2") {

      val shape = Shape ><
        LeafArity.Literal(2) :<<- "x" ><
        LeafArity.Literal(3) :<<- "y"

      val record = shape.record

      //      VizType.infer(static).treeString.shouldBe()

      typeInferShort(record.keys).shouldBe(
        """
          |String("y") :: String("x") :: HNil""".stripMargin
      )

      typeInferShort(record.values).shouldBe(
        """
          |LeafArity.Literal[Int(3)] :: LeafArity.Literal[Int(2)] :: HNil
          |""".stripMargin
      )

      assert(record.get("x") == LeafArity.Literal(2))
    }
  }

  describe("record") {

    it("1") {

      val shape = Shape ><
        LeafArity.Literal(2) :<<- "x"

      val record = shape.record

//      VizType.infer(record).treeString.shouldBe()

      typeInferShort(record.keys).shouldBe(
        """
          |String("x") :: HNil""".stripMargin
      )

      typeInferShort(record.values).shouldBe(
        """
          |LeafArity.Literal[Int(2)] :: HNil""".stripMargin
      )

      assert(record.get("x") == LeafArity.Literal(2))
    }

    it("2") {

      val shape = Shape ><
        LeafArity.Literal(2) :<<- "x" ><
        LeafArity.Literal(3) :<<- "y"

      val record = shape.record

      //      VizType.infer(static).treeString.shouldBe()

      typeInferShort(record.keys).shouldBe(
        """
          |String("y") :: String("x") :: HNil""".stripMargin
      )

      typeInferShort(record.values).shouldBe(
        """
          |LeafArity.Literal[Int(3)] :: LeafArity.Literal[Int(2)] :: HNil
          |""".stripMargin
      )

      assert(record.get("x") == LeafArity.Literal(2))
    }
  }

  describe("names") {

    val shape = Shape ><
      LeafArity.Literal(2) :<<- "x" cross
      LeafArity.Literal(3) :<<- "y"

    it("1") {

      val nn = shape.names

      assert(nn.headName == "y")
    }
  }

  describe("values") {

    val shape = Shape ><
      LeafArity.Literal(2) :<<- "x" ><
      LeafArity.Literal(3) :<<- "y"

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
      LeafArity.Literal(2) :<<- "x" ><
      LeafArity.Literal(3) :<<- "y"

    val ab = Names >< "a" >< "b"
    val ij = Names >< "i" >< "j"
    val namesTooMany = Names >< "a" >< "b" >< "c"

    val shapeRenamed = Shape ><
      LeafArity.Literal(2) :<<- "a" ><
      LeafArity.Literal(3) :<<- "b"

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
      LeafArity.Literal(2) :<<- "x" ><
      LeafArity.Literal(3) :<<- "y" ><
      LeafArity.Literal(4) :<<- "z"

    it("getByIndex") {

      val v = shape.Sub(0).axis

      assert(v == LeafArity.Literal(4) :<<- "z") // HList is of inverse order
    }

    it("getByName") {

      val v = shape.Sub.apply("y").axis

      assert(v == LeafArity.Literal(3) :<<- "y")
    }
  }

  describe("concat") {

    it("1") {

      val s1 = Shape ><
        LeafArity.Literal(2) :<<- "x"
//        Arity.FromLiteral(3) :<<- "y"

      val s2 = Shape ><
        LeafArity.Literal(2) :<<- "i"
//        Arity.FromLiteral(3) :<<- "j"

      val r = s1 >< s2

      typeInferShort(r).shouldBe(
        """
          |Shape.eye.type >< (LeafArity.Literal[Int(2)] :<<- String("x")) >< (LeafArity.Literal[Int(2)] :<<- String("i"))""".stripMargin
      )
    }

    it("2") {

      val s1 = Shape ><
        LeafArity.Literal(2) :<<- "x" ><
        LeafArity.Literal(3) :<<- "y"

      val s2 = Shape ><
        LeafArity.Literal(2) :<<- "i" ><
        LeafArity.Literal(3) :<<- "j"

      val r = s1 concat s2

      typeInferShort(r).shouldBe(
        """
          |
          |Shape.eye.type >< (LeafArity.Literal[Int(2)] :<<- String("x")) >< (LeafArity.Literal[Int(3)] :<<- String("y")) ><
          | (LeafArity.Literal[Int(2)] :<<- String("i")) >< (LeafArity.Literal[Int(3)] :<<- String("j"))
          |""".stripMargin.split('\n').mkString
      )
    }
  }

  describe("transpose") {

    it("1") {

      val shape = Shape ><
        Arity(1) :<<- "a" ><
        Arity(2) :<<- "b" ><
        Arity(3) :<<- "c"

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
