package org.shapesafe.m

import com.tribbloids.graph.commons.testlib.BaseSpec
import com.tribbloids.graph.commons.util.viz.TypeViz
import shapeless.Witness

class NameOfSpec extends BaseSpec {

  val w1 = NameOf.TypeConstructor[Int]

  import NameOfSpec._

  describe("TypeConstructorNameOf") {
    it("Int") {

      val gd = TypeViz.infer(Witness("Int"))
      gd.typeStr.shouldBe(
        """shapeless.Witness{type T = String("Int")} ≅ shapeless.Witness.Aux[String("Int")]"""
      )
      TypeViz.infer(w1).should_=:=(gd)
    }

    it("generic 1") {

      val w1 = NameOf.TypeConstructor[^^[_, _]]

      TypeViz
        .infer(w1)
        .typeStr
        .shouldBe(
          "shapeless.Witness{type T = String(\"AsStringSpec.^^\")} ≅ shapeless.Witness.Aux[String(\"AsStringSpec.^^\")]"
        )
    }

    it("generic 2") {

      val e1 = ^^[Int, String]()

      TypeViz
        .infer(e1.TConstructors.nameOfSelf)
        .typeStr
        .shouldBe(
          "shapeless.Witness{type T = String(\"AsStringSpec.^^\")} ≅ shapeless.Witness.Aux[String(\"AsStringSpec.^^\")]"
        )
    }

    // TODO: doesn't work
//    it("generic type arg") {
//
//      val e1 = ^^[Int, String]()
//
//      TypeViz.infer(e1.TConstructors.nameOfT1).typeStr.shouldBe()
//    }
  }

  // TODO: doesn't work
  describe("TypeNameOf") {}

}

object NameOfSpec {

  case class ^^[T1, T2]() {

    object TConstructors {

      final val nameOfT1 = NameOf.TypeConstructor[T1]

      final val nameOfSelf = NameOf.TypeConstructor[^^[T1, T2]]
    }

  }
}
