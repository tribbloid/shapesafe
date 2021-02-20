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

  it("create") {

    require(names.static == hList)

    val t1 = VizType.infer(names.static)
    val t2 = VizType.infer(hList)

    assert(t1.tt =:= t2.tt)
  }

  it("from String literal") {

    import Names.Syntax._

    val v1 = Names >< "x" >< "y" >< "z"
    val v2 = literalToInfix("x") >< "y" >< "z"
    val v3 = "x" >< "y" >< "z"

    assert(v1 == v2)
    assert(v2 == v3)
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

  it("FromStatic") {

    val names2 = Names.FromStatic(hList)

    // TODO: runtime assertion?

    val t1 = VizType.infer(names)
    val t2 = VizType.infer(names2)

    t1.shouldBe(t2)
  }

  it("as Indices") {

    VizType[Names.Eye.Canonical].shouldBe(VizType[Indices.Eye])

    val ii = Indices >< Name("x") >< Name("y") >< Name("z")

    VizType.infer(names.canonical).shouldBe(VizType.infer(ii))

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
