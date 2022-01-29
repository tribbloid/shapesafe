package shapesafe.core.shape

import shapesafe.BaseSpec
import shapesafe.core.arity.Arity
import shapeless.HNil

class StaticShapeSpec extends BaseSpec {

  import shapeless.record._

  describe("construct") {

    it("named") {

      val shape = Shape &
        Arity(2) :<<- "x" and
        Arity(3) :<<- "y"

      typeInferShort(shape.shapeType).shouldBe(
        """
          |StaticShape.Eye >< (ConstArity.Literal[Int(2)] :<<- String("x")) >< (ConstArity.Literal[Int(3)] :<<- String("y"))
          |""".stripMargin
      )

//      VizType.infer(shape.static).toString.shouldBe()

      assert(shape.record.apply("x").runtimeValue == 2)
    }

    it("nameless") {

      val shape = Shape &
        Arity(2) and
        Arity(3)

      typeInferShort(shape.shapeType).shouldBe(
        """
          |StaticShape.Eye >< Arity.^[ConstArity.Literal[Int(2)]] >< Arity.^[ConstArity.Literal[Int(3)]]
          |""".stripMargin
      )

    }

    it("mixed") {

      val shape = Shape &
        Arity(2) :<<- "x" &
        Arity(3) and
        Arity(4) :<<- "z"

      assert(shape.record.apply("x").runtimeValue == 2)
      assert(shape.record.apply("z").runtimeValue == 4)

      typeInferShort(shape.shapeType).shouldBe(
        """
          |StaticShape.Eye >< (ConstArity.Literal[Int(2)] :<<- String("x")) >< Arity.^[ConstArity.Literal[Int(3)]] >< (ConstArity.Literal[Int(4)] :<<- String("z"))
          |""".stripMargin
      )
    }

    it("nameless shortcut") {

      val shape = Shape & 2 & 3
      typeInferShort(shape.shapeType).shouldBe(
        """
          |StaticShape.Eye >< Arity.^[ConstArity.Literal[Int(2)]] >< Arity.^[ConstArity.Literal[Int(3)]]
          |""".stripMargin
      )
    }
  }

  it("toString") {
    val shape = Shape &
      Arity(2) :<<- "x" &
      Arity(3) and
      Arity(4) :<<- "z"

    shape.toString.shouldBe(
      """
        |2:Literal :<<- x ><
        |  3:Literal ><
        |  4:Literal :<<- z
        |""".stripMargin
    )
  }

  describe(StaticShape.FromStatic.getClass.getSimpleName) {

    it("from HNil") {

      val hh = HNil

      val shape = StaticShape.FromStatic(hh)

      assert(shape == StaticShape.Eye)
    }

    it("1") {

      val hh = (Arity(3) :<<- "x") ::
        HNil

      val shape = StaticShape.FromStatic(hh)

      //    VizType.infer(shape).toString.shouldBe()

      assert(shape.static == hh)
    }

    it("2") {

      val hh = (Arity(3) :<<- "x") ::
        (Arity(4) :<<- "y") ::
        HNil

      val shape = StaticShape.FromStatic(hh)

      //    VizType.infer(shape).toString.shouldBe()

      assert(shape.static == hh)
      assert(shape.static.head.nameless == Arity(3))
    }
  }

  describe(StaticShape.FromAxes.getClass.getSimpleName) {

    import shapeless.syntax.singleton.mkSingletonOps

    it("from HNil") {

      val hh = HNil

      val shape = StaticShape.FromAxes(hh)

      assert(shape == StaticShape.Eye)
    }

    it("1") {

      val hh = ("x" ->> Arity(3).arityType) ::
        HNil

      val shape = StaticShape.FromAxes(hh)

      //    VizType.infer(shape).toString.shouldBe()

      assert(shape.dimensions.static == hh)
    }

    it("2") {

      val hh = ("x" ->> Arity(3).arityType) ::
        ("y" ->> Arity(4).arityType) ::
        HNil

      val shape = StaticShape.FromAxes(hh)

      //    VizType.infer(shape).toString.shouldBe()

      assert(shape.dimensions.static == hh)
      assert(shape.static.head.nameless == Arity(3))
    }

    it("with empty name") {

      val hh = ("x" ->> Arity(3).arityType) ::
        ("" ->> Arity(4).arityType) ::
        HNil

      val shape = StaticShape.FromAxes(hh)

      typeInferShort(shape).toString.shouldBe(
        """StaticShape.Eye >< Arity.^[ConstArity.Literal[Int(4)]] >< (ConstArity.Literal[Int(3)] :<<- String("x"))"""
      )
      //    VizType.infer(shape).toString.shouldBe()
    }
  }

  describe(Shape.Literals.getClass.getSimpleName) {

    it("1") {
      val ss = Shape.Literals(4)

      ss.dimensions.static.head.requireEqual(4)
      ss.dimensions.static.last.requireEqual(4)

      val nn = (ss :<<= (Names >< "i")).eval

      nn.toString.shouldBe(
        """
          |4:Literal :<<- i
          |""".stripMargin
      )
    }

    it("2") {
      val ss = Shape.Literals(4, 3, 2)

      ss.dimensions.static.head.requireEqual(2)
      ss.dimensions.static.last.requireEqual(4)

      val nn = (ss :<<= (Names >< "i" >< "j" >< "k")).eval

      nn.toString.shouldBe(
        """
          |4:Literal :<<- i ><
          |  3:Literal :<<- j ><
          |  2:Literal :<<- k
          |""".stripMargin
      )
    }
  }

  describe(Shape.Nats.getClass.getSimpleName) {

    it("1") {
      val ss = Shape.Nats(4)

      ss.dimensions.static.head.requireEqual(4)
      ss.dimensions.static.last.requireEqual(4)

      val nn = (ss :<<= (Names >< "i")).eval

      nn.toString.shouldBe(
        """
          |4:Derived :<<- i
          |""".stripMargin
      )
    }

    it("2") {
      val ss = Shape.Nats(4, 3, 2)

      ss.dimensions.static.head.requireEqual(2)
      ss.dimensions.static.last.requireEqual(4)

      val nn = (ss :<<= (Names >< "i" >< "j" >< "k")).eval

      nn.toString.shouldBe(
        """
          |4:Derived :<<- i ><
          |  3:Derived :<<- j ><
          |  2:Derived :<<- k
          |""".stripMargin
      )
    }

//    it("3") {
//      val ss = (Shape.Literals(4, 3, 2) :<<= (Names >< "i" >< "j" >< "k")).eval
//
//      ss.dimensions.static.head.core.requireEqual(2)
//      ss.dimensions.static.last.core.requireEqual(4)
//    }
  }

  describe("index") {

    it("1") {

      val shape = Shape &
        Arity(2) :<<- "x"

      val record = shape.record

      //      VizType.infer(record).treeString.shouldBe()

      typeInferShort(record.keys).shouldBe(
        """
          |String("x") :: HNil""".stripMargin
      )

      typeInferShort(record.values).shouldBe(
        """
          |ConstArity.Literal[Int(2)] :: HNil""".stripMargin
      )

      assert(record.get("x") == Arity(2).arityType)
    }

    it("2") {

      val shape = Shape &
        Arity(2) :<<- "x" &
        Arity(3) :<<- "y"

      val record = shape.record

      //      VizType.infer(static).treeString.shouldBe()

      typeInferShort(record.keys).shouldBe(
        """
          |String("y") :: String("x") :: HNil""".stripMargin
      )

      typeInferShort(record.values).shouldBe(
        """
          |ConstArity.Literal[Int(3)] :: ConstArity.Literal[Int(2)] :: HNil
          |""".stripMargin
      )

      assert(record.get("x").^.nameless == Arity(2))
    }
  }

  describe("record") {

    it("1") {

      val shape = Shape &
        Arity(2) :<<- "x"

      val record = shape.record

//      VizType.infer(record).treeString.shouldBe()

      typeInferShort(record.keys).shouldBe(
        """
          |String("x") :: HNil""".stripMargin
      )

      typeInferShort(record.values).shouldBe(
        """
          |ConstArity.Literal[Int(2)] :: HNil""".stripMargin
      )

      assert(record.get("x") == Arity(2).arityType)
    }

    it("2") {

      val shape = Shape &
        Arity(2) :<<- "x" &
        Arity(3) :<<- "y"

      val record = shape.record

      //      VizType.infer(static).treeString.shouldBe()

      typeInferShort(record.keys).shouldBe(
        """
          |String("y") :: String("x") :: HNil""".stripMargin
      )

      typeInferShort(record.values).shouldBe(
        """
          |ConstArity.Literal[Int(3)] :: ConstArity.Literal[Int(2)] :: HNil
          |""".stripMargin
      )

      assert(record.get("x") == Arity(2).arityType)
    }
  }

  describe("names") {

    val shape = Shape &
      Arity(2) :<<- "x" and
      Arity(3) :<<- "y"

    it("1") {

      val nn = shape.names

      assert(nn.head == "y")
    }
  }

  describe("values") {

    val shape = Shape &
      Arity(2) :<<- "x" &
      Arity(3) :<<- "y"

    it("1") {

      val vv = shape.dimensions

      assert(vv.head == Arity(3).arityType)
    }
  }

  describe("peek & interrupt") {

    it("Eye") {

      shouldNotCompile(
        """Shape.interrupt""",
        """.*(∅).*"""
      )
    }

    it("1") {

      val s = Shape(1, 2)

      shouldNotCompile(
        """s.interrupt""",
        """.*(1 >< 2).*"""
      )
    }

    it("2") {

      val s = Shape(1, 2).:<<=*("a", "b").eval

      shouldNotCompile(
        """s.interrupt""",
        """.*(\Q1 :<<- a >< (2 :<<- b)\E).*"""
      )
    }
  }
}
