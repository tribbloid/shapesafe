package org.shapesafe.core.shape

import com.tribbloids.graph.commons.util.viz.TypeViz
import org.shapesafe.BaseSpec
import org.shapesafe.core.arity.{Arity, LeafArity}
import org.shapesafe.core.shape.LeafShape.{FromLiterals, FromNats, FromRecord, FromStatic}
import shapeless.HNil

class LeafShapeSpec extends BaseSpec {

  import shapeless.record._

  describe("create") {

    it("named") {

      val shape = Shape >|<
        LeafArity.Literal(2) :<<- "x" append
        LeafArity.Literal(3) :<<- "y"

      typeInferShort(shape).shouldBe(
        """
          |LeafShape.Eye >< (LeafArity.Literal[Int(2)] :<<- String("x")) >< (LeafArity.Literal[Int(3)] :<<- String("y"))
          |""".stripMargin
      )

//      VizType.infer(shape.static).toString.shouldBe()

      assert(shape.record.apply("x").runtimeArity == 2)
    }

    it("nameless") {

      val shape = Shape >|<
        LeafArity.Literal(2) append
        LeafArity.Literal(3)

      typeInferShort(shape).shouldBe(
        """
          |LeafShape.Eye >< (LeafArity.Literal[Int(2)] :<<- noName.type) >< (LeafArity.Literal[Int(3)] :<<- noName.type)
          |""".stripMargin
      )

    }

    it("mixed") {

      val shape = Shape >|<
        LeafArity.Literal(2) :<<- "x" >|<
        LeafArity.Literal(3) append
        LeafArity.Literal(4) :<<- "z"

      assert(shape.record.apply("x").runtimeArity == 2)
      assert(shape.record.apply("z").runtimeArity == 4)

      typeInferShort(shape).shouldBe(
        """
          |LeafShape.Eye >< (LeafArity.Literal[Int(2)] :<<- String("x")) >< (LeafArity.Literal[Int(3)] :<<- noName.type) >< (LeafArity.Literal[Int(4)] :<<- String("z"))
          |""".stripMargin
      )
    }
  }

  it("toString") {
    val shape = Shape >|<
      Arity(2) :<<- "x" >|<
      Arity(3) append
      Arity(4) :<<- "z"

    shape.toString.shouldBe(
      """
        |Eye ><
        |  2:Literal :<<- x ><
        |  3:Literal ><
        |  4:Literal :<<- z
        |""".stripMargin
    )
  }

  describe(FromStatic.getClass.getSimpleName) {

    it("from HNil") {

      val hh = HNil

      val shape = LeafShape.FromStatic(hh)

      assert(shape == LeafShape.Eye)
    }

    it("1") {

      val hh = (Arity(3) :<<- "x") ::
        HNil

      val shape = LeafShape.FromStatic(hh)

      //    VizType.infer(shape).toString.shouldBe()

      assert(shape.static == hh)
    }

    it("2") {

      val hh = (Arity(3) :<<- "x") ::
        (Arity(4) :<<- "y") ::
        HNil

      val shape = LeafShape.FromStatic(hh)

      //    VizType.infer(shape).toString.shouldBe()

      assert(shape.static == hh)
      assert(shape.static.head.dimension == Arity(3))
    }
  }

  describe(FromRecord.getClass.getSimpleName) {

    import shapeless.syntax.singleton.mkSingletonOps

    it("from HNil") {

      val hh = HNil

      val shape = LeafShape.FromRecord(hh)

      assert(shape == LeafShape.Eye)
    }

    it("1") {

      val hh = ("x" ->> Arity(3)) ::
        HNil

      val shape = LeafShape.FromRecord(hh)

      //    VizType.infer(shape).toString.shouldBe()

      assert(shape.dimensions.static == hh)
    }

    it("2") {

      val hh = ("x" ->> Arity(3)) ::
        ("y" ->> Arity(4)) ::
        HNil

      val shape = LeafShape.FromRecord(hh)

      //    VizType.infer(shape).toString.shouldBe()

      assert(shape.dimensions.static == hh)
      assert(shape.static.head.dimension == Arity(3))
    }
  }

  describe(FromLiterals.getClass.getSimpleName) {

    it("1") {
      val ss = Shape.Literals(4)

      ss.dimensions.static.head.internal.requireEqual(4)
      ss.dimensions.static.last.internal.requireEqual(4)

      val nn = (ss |<<- (Names >< "i")).eval

      nn.toString.shouldBe(
        """
          |Eye ><
          |  4:Literal :<<- i
          |""".stripMargin
      )
    }

    it("2") {
      val ss = Shape.Literals(4, 3, 2)

      ss.dimensions.static.head.internal.requireEqual(2)
      ss.dimensions.static.last.internal.requireEqual(4)

      val nn = (ss |<<- (Names >< "i" >< "j" >< "k")).eval

      nn.toString.shouldBe(
        """
          |Eye ><
          |  4:Literal :<<- i ><
          |  3:Literal :<<- j ><
          |  2:Literal :<<- k
          |""".stripMargin
      )
    }
  }

  describe(FromNats.getClass.getSimpleName) {

    it("1") {
      val ss = Shape.Nats(4)

      ss.dimensions.static.head.internal.requireEqual(4)
      ss.dimensions.static.last.internal.requireEqual(4)

      val nn = (ss |<<- (Names >< "i")).eval

      nn.toString.shouldBe(
        """
          |Eye ><
          |  4:Derived :<<- i
          |""".stripMargin
      )
    }

    it("2") {
      val ss = Shape.Nats(4, 3, 2)

      ss.dimensions.static.head.internal.requireEqual(2)
      ss.dimensions.static.last.internal.requireEqual(4)

      val nn = (ss |<<- (Names >< "i" >< "j" >< "k")).eval

      nn.toString.shouldBe(
        """
          |Eye ><
          |  4:Derived :<<- i ><
          |  3:Derived :<<- j ><
          |  2:Derived :<<- k
          |""".stripMargin
      )
    }

//    it("3") {
//      val ss = (Shape.Literals(4, 3, 2) |<<- (Names >< "i" >< "j" >< "k")).eval
//
//      ss.dimensions.static.head.internal.requireEqual(2)
//      ss.dimensions.static.last.internal.requireEqual(4)
//    }
  }

  describe("index") {

    it("1") {

      val shape = Shape >|<
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

      val shape = Shape >|<
        LeafArity.Literal(2) :<<- "x" >|<
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

      val shape = Shape >|<
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

      val shape = Shape >|<
        LeafArity.Literal(2) :<<- "x" >|<
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

    val shape = Shape >|<
      LeafArity.Literal(2) :<<- "x" append
      LeafArity.Literal(3) :<<- "y"

    it("1") {

      val nn = shape.names

      assert(nn.head == "y")
    }
  }

  describe("values") {

    val shape = Shape >|<
      LeafArity.Literal(2) :<<- "x" >|<
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

  describe("Sub") {

    val shape = Shape >|<
      LeafArity.Literal(2) :<<- "x" >|<
      LeafArity.Literal(3) :<<- "y" >|<
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
}
