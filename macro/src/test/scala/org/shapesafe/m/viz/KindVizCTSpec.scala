package org.shapesafe.m.viz

import com.tribbloids.graph.commons.testlib.BaseSpec
import com.tribbloids.graph.commons.util.reflect.Reflection
import com.tribbloids.graph.commons.util.reflect.format.FormatProtos.{DeAlias, HidePackages}
import com.tribbloids.graph.commons.util.reflect.format.FormatOvrd.{~~, Only}
import com.tribbloids.graph.commons.util.viz.TypeViz
import shapeless.Witness

class KindVizCTSpec extends BaseSpec {

  import KindVizCTSpec._

  val gd = viz.infer(Witness("Int").value)

  describe("Stub") {

    it("ground truth") {

      gd.typeStr.shouldBe(
        """String("Int")"""
      )
    }

    it("Int") {

      val w1 = stub.infoOf[Int]
      viz[w1.Out].should_=:=(gd)
    }

    it(" ... implicitly") {

      val w1 = stub[Int].summon

      //      viz.infer(w1).should_=:=()
      viz[w1.Out].should_=:=(gd)
    }

    it("generic 1") {

      val w1 = stub.infoOf[Dummy[_, _]]

      viz[w1.Out].typeStr
        .shouldBe(
          s"""String("KindVizCTSpec.Dummy")"""
        )
    }

    it("generic 2") {

      val e1 = Dummy[Int, String]()

      viz[e1.infoOf.type#Out].typeStr
        .shouldBe(
          s"""String("KindVizCTSpec.Dummy")"""
        )
    }
  }

  describe("Ovrd") {

    it("1") {

      val w1 = ovrd.infoOf[Dummy[Int, String]]

      viz[w1.Out].typeStr
        .shouldBe(
          s"""String("Int a b String")"""
        )
    }

    it(" ... implicitly") {

      val w1 = ovrd[Dummy[Int, String]].summon

      viz[w1.Out].typeStr
        .shouldBe(
          s"""String("Int a b String")"""
        )
    }

    describe("abort on error") {

      val localV = "a"

      it("1") {

        val w1 = ovrd.infoOf[Only[localV.type]]
        viz[w1.Out].typeStr
          .shouldBe(
            s"""String("FormatOvrd.Only")"""
          )
      }

      it("2") {
        val w1 = ovrd.infoOf[Only[nonFinalV.type]]
        viz[w1.Out].typeStr
          .shouldBe(
            s"""String("FormatOvrd.Only")"""
          )
      }

    }
  }
}

object KindVizCTSpec {

  val viz: TypeViz[Reflection.Runtime.type] = TypeViz.formattedBy {
    import com.tribbloids.graph.commons.util.reflect.format.Formats._

    TypeInfo ~ DeAlias ~ HidePackages
  }
  val stub = KindVizCT.Stub
  val ovrd = KindVizCT.Ovrd

  final val finalV = "b"
  val nonFinalV = "b"

  case class Dummy[T1, T2]() extends (T1 ~~ Only["a"] ~~ Only[finalV.type] ~~ T2) {

    final val infoOf = stub.infoOf[Dummy[T1, T2]]
  }

  object Dummy {
    lazy val name: String = classOf[Dummy[_, _]].getCanonicalName
  }
}
