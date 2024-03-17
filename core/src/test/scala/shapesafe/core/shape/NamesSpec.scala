package shapesafe.core.shape

import shapesafe.BaseSpec
import shapesafe.core.shape.Index.Name
import shapesafe.core.shape.Indices.tupleExtension
import shapeless.HNil

class NamesSpec extends BaseSpec {

  import shapeless.syntax.singleton._

  val names = Names >< "x" >< "y" >< "z"

  val hList = "z".narrow :: "y".narrow :: "x".narrow :: HNil

  describe("construct") {

    it("1") {

      require(names.static == hList)

      val t1 = TypeViz.infer(names.static)
      val t2 = TypeViz.infer(hList)

      t1.===!(t2)
    }

    it("2") {

      val n2 = Names("x", "y", "z")

      val t1 = TypeViz.infer(names)
      val t2 = TypeViz.infer(n2)

      t1.===!(t2)
    }

    it("3") {

      import Names.Syntax._

      val n2 = "x" >< "y" >< "z"

      val t1 = TypeViz.infer(names)
      val t2 = TypeViz.infer(n2)

      t1.===!(t2)
    }
  }

  it("cons") {

//    shouldNotCompile( // TODO : enable this after
//      """implicitly[Names.Cons[Names.Eye, String]]"""
//    )

    val w = "a"
    val hh = Names.Eye >< w

    hh.toString
      .shouldBe(
        """
        |a
        |""".stripMargin
      )
  }

  it("FromLiterals") {

    val names2 = Names.FromLiterals(hList)

    // TODO: runtime assertion?

    val t1 = TypeViz.infer(names)
    val t2 = TypeViz.infer(names2)

    t1.===!(t2)
  }

  it("as Indices") {

    TypeViz[Names.Eye.AsIndices].should_=:=(TypeViz[Indices.Eye])

    val ii = Indices >< Name("x") >< Name("y") >< Name("z")

    TypeViz.infer(names.asIndices).===!(TypeViz.infer(ii))

    type P = String <:< Int
//    implicitly[Names.Eye <:< Indices.Eye]
//
////    val namesT = WideTyped(names)
//    val namesT = WideTyped(Names >< "x")
//
//    implicitly[namesT.Wide <:< Indices.Impl]
//
////    val indicesT = WideTyped(Indices >< Name("x") >< Name("y") >< Name("z"))
//    val indicesT = WideTyped(Indices >< Name("x"))
//
//    VizType[indicesT.Wide].shouldBe()
//
//    implicitly[namesT.Wide <:< indicesT.Wide]
  }
}
