package org.shapesafe.m

import com.tribbloids.graph.commons.testlib.BaseSpec
import com.tribbloids.graph.commons.util.viz.TypeViz
import shapeless.Witness

class Type2StringSpec extends BaseSpec {

  val gd = Witness("Int")
  val gdViz = TypeViz.infer(gd)

  val w1 = Type2String[Int]

  import Type2StringSpec._

  it("ground truth") {

    gdViz.typeStr.shouldBe(
      """shapeless.Witness{type T = String("Int")} ≅ shapeless.Witness.Aux[String("Int")]"""
    )
  }

  it("Int") {

    TypeViz.infer(w1).should_=:=(gdViz)
  }

  // TODO: these doesn't work
  it("generic type arg(s)") {

    val e1 = ^^[Int, String]()

//    TypeViz.infer(e1.wT1).should_=:=()
//
//    TypeViz.infer(e1.wTSelf).should_=:=()
  }
}

object Type2StringSpec {

  case class ^^[T1, T2]() {

    final val wT1 = Type2String[T1]

    final val wTSelf = Type2String[^^[T1, T2]]
  }
}
