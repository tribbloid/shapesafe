package org.shapesafe.core.shape

import com.tribbloids.graph.commons.util.viz.VizType
import org.shapesafe.BaseSpec
import org.shapesafe.core.shape.Index.Name
import org.shapesafe.core.shape.Indices.Infix
import shapeless.{HNil, Witness}

class NamesSpec extends BaseSpec {

  import shapeless.syntax.singleton._

  val names = Names >< "x" >< "y" >< "z"

  val hList = "z".narrow :: "y".narrow :: "x".narrow :: HNil

  describe("create") {

    it("1") {

      require(names.static == hList)

      val t1 = VizType.infer(names.static)
      val t2 = VizType.infer(hList)

      t1.===!===(t2)
    }

    it("2") {

      val n2 = Names("x", "y", "z")

      val t1 = VizType.infer(names)
      val t2 = VizType.infer(n2)

      t1.===!===(t2)
    }

    it("3") {

      import Names.Syntax._

      val n2 = "x" >< "y" >< "z"

      val t1 = VizType.infer(names)
      val t2 = VizType.infer(n2)

      t1.===!===(t2)
    }
  }

  it("cons") {

//    shouldNotCompile( // TODO : enable this after
//      """implicitly[Names.Cons[Names.Eye, String]]"""
//    )

    val w = Witness("a")
    val hh = implicitly[Names.Cons[Names.Eye, w.T]]

    hh.apply(Names.Eye, w.value)
      .toString
      .shouldBe(
        """
        |Eye ><
        |  a
        |""".stripMargin
      )
  }

  it("FromLiterals") {

    val names2 = Names.FromLiterals(hList)

    // TODO: runtime assertion?

    val t1 = VizType.infer(names)
    val t2 = VizType.infer(names2)

    t1.===!===(t2)
  }

  it("as Indices") {

    VizType[Names.Eye.AsIndices].shouldBe(VizType[Indices.Eye])

    val ii = Indices >< Name("x") >< Name("y") >< Name("z")

    VizType.infer(names.asIndices).===!===(VizType.infer(ii))

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
