package org.shapesafe.m

import com.tribbloids.graph.commons.testlib.BaseSpec
import com.tribbloids.graph.commons.util.reflect.TypeFormat
import com.tribbloids.graph.commons.util.viz.TypeViz
import shapeless.Witness

class GetInfoOfSpec extends BaseSpec {

  val viz = TypeViz.withFormat(TypeFormat(variants = TypeFormat.variants.DeAlias))
  val w1 = GetInfoOf.TypeConstructor[Int]

  import GetInfoOfSpec._

//  it("spike") {
//
//    val tt = Witness.`abc`
//    type T = Witness.`abc`
//
//    TypeViz.infer(tt).should_=:=()
//    TypeViz[T].should_=:=()
//  }

  describe("TypeConstructorNameOf") {
    it("Int") {

      val gd = viz.infer(Witness("Int").value)
      gd.typeStr.shouldBe(
        """String("Int")"""
      )
      viz[w1.Out].should_=:=(gd)
    }

    it("generic 1") {

      val w1 = GetInfoOf.TypeConstructor[Dummy[_, _]]

      viz[w1.Out].typeStr
        .shouldBe(
          """String("GetInfoOfSpec.Dummy")"""
        )
    }

    it("generic 2") {

      val e1 = Dummy[Int, String]()

      viz[e1.TConstructors.nameOfSelf.type#Out].typeStr
        .shouldBe(
          """String("GetInfoOfSpec.Dummy")"""
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

object GetInfoOfSpec {

  case class Dummy[T1, T2]() {

    object TConstructors {

      final val nameOfT1 = GetInfoOf.TypeConstructor[T1]

      final val nameOfSelf = GetInfoOf.TypeConstructor[Dummy[T1, T2]]
    }

  }
}
