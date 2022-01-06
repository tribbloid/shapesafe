package shapesafe.m.viz

import ai.acyclic.graph.commons.reflect.format.FormatOvrd.{~~, Only}
import shapesafe.m.GenericMsgEmitter
import shapeless.Witness
import singleton.ops.+

class KindVizCTSpec extends VizCTSpec {

  lazy val VizCT: KindVizCT.type = KindVizCT

  import KindVizCTSpec._

  describe(VizCT.toString) {

    it("1") {

      val w1 = VizCT.infoOf[Int]

      type T1 = w1.Out
      implicitly[T1 + ""].value.toString.shouldBe(
        """
          |-+ Int
          | !-+ AnyVal
          |   !-- Any
          |""".stripMargin
      )
    }

    it(" ... implicitly") {

      val w1 = VizCT[Int].summonInfo

      type T1 = w1.Out
      implicitly[T1 + ""].value.toString.shouldBe(
        """
          |-+ Int
          | !-+ AnyVal
          |   !-- Any
          |""".stripMargin
      )
    }
  }

  describe(NoTree.toString) {

    val groundTruth = TypeVizShort.infer(Witness("Int").value)

    it("ground truth") {

      groundTruth.typeStr.shouldBe(
        """String("Int")"""
      )
    }

    it("1") {

      val w1 = NoTree.infoOf[Int]
      TypeVizShort[w1.Out].should_=:=(groundTruth)
    }

    it(" ... implicitly") {

      val w1 = NoTree[Int].summonInfo

      //      viz.infer(w1).should_=:=()
      TypeVizShort[w1.Out].should_=:=(groundTruth)
    }

    it("generic 1") {

      val w1 = NoTree.infoOf[Dummy[_, _]]

      TypeVizShort[w1.Out].typeStr
        .shouldBe(
          s"""String("KindVizCTSpec.Dummy")"""
        )
    }

    it("summonStr") {

      val str = NoTree[Int].summonStr
      str.shouldBe("Int")
    }

    it("peek (explicit)") {

      val info = NoTree[Int].summonInfo

      GenericMsgEmitter.byOnlyInstance[info.Out, GenericMsgEmitter.Info]
    }

    it("peek") {

      NoTree[Int].peek
    }

    it("interrupt") {

      val viz = NoTree[Int]
      shouldNotCompile(
        """viz.interrupt""",
        ".*(Int).*"
      )
    }
  }

  describe(WithOvrd.toString) {

    it("1") {

      val w1 = WithOvrd.infoOf[Dummy[Int, String]]

      TypeVizShort[w1.Out].typeStr
        .shouldBe(
          s"""String("Int a b String")"""
        )
    }

    it(" ... implicitly") {

      val w1 = WithOvrd[Dummy[Int, String]].summonInfo

      TypeVizShort[w1.Out].typeStr
        .shouldBe(
          s"""String("Int a b String")"""
        )
    }

    describe("abort on error") {

      val localV = "a"

      it("1") {

        val w1 = WithOvrd.infoOf[Only[localV.type]]
        TypeVizShort[w1.Out].typeStr
          .shouldBe(
            s"""String("FormatOvrd.Only")"""
          )
      }

      it("2") {
        val w1 = WithOvrd.infoOf[Only[nonFinalV.type]]
        TypeVizShort[w1.Out].typeStr
          .shouldBe(
            s"""String("FormatOvrd.Only")"""
          )
      }
    }
  }
}

object KindVizCTSpec {

  val NoTree = KindVizCT.NoTree
  val WithOvrd = KindVizCT.WithOvrd

  final val finalV = "b"
  val nonFinalV = "b"

  case class Dummy[T1, T2]() extends (T1 ~~ Only["a"] ~~ Only[finalV.type] ~~ T2) {

//    final val infoOf = KindVizCT.infoOf[Dummy[T1, T2]]
  }

  object Dummy {
    lazy val name: String = classOf[Dummy[_, _]].getCanonicalName
  }
}
