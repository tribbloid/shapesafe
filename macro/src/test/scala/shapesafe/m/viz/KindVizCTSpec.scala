package shapesafe.m.viz

import ai.acyclic.prover.commons.reflect.format.FormatOvrd.{~~, SingletonName}
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
          |+ Int
          |!-+ AnyVal
          |  !-- Any
          |""".stripMargin
      )
    }

    it(" ... implicitly") {

      val w1 = VizCT[Int].summonInfo

      type T1 = w1.Out
      implicitly[T1 + ""].value.toString.shouldBe(
        """
          |+ Int
          |!-+ AnyVal
          |  !-- Any
          |""".stripMargin
      )
    }
  }

  describe(kindNoTree.toString) {

    val groundTruth = TypeVizShort.infer["Int"](Witness("Int").value)

    it("ground truth") {

      groundTruth.typeStr.shouldBe(
        """String("Int")"""
      )
    }

    it("1") {

      val w1 = kindNoTree.infoOf[Int]
      TypeVizShort[w1.Out].should_=:=(groundTruth)
    }

    it(" ... implicitly") {

      val w1 = kindNoTree[Int].summonInfo

      //      viz.infer(w1).should_=:=()
      TypeVizShort[w1.Out].should_=:=(groundTruth)
    }

    it("generic 1") {

      val w1 = kindNoTree.infoOf[Dummy[_, _]]

      TypeVizShort[w1.Out].typeStr
        .shouldBe(
          s"""String("KindVizCTSpec.Dummy")"""
        )
    }

    it("summonStr") {

      val str = kindNoTree[Int].summonStr
      str.shouldBe("Int")
    }

    it("peek (explicit)") {

      val info = kindNoTree[Int].summonInfo

      GenericMsgEmitter.byOnlyInstance[info.Out, GenericMsgEmitter.Info]
    }

    it("peek") {

      kindNoTree[Int].peek
    }

    it("interrupt") {

      val viz = kindNoTree[Int]
      shouldNotCompile(
        """viz.interrupt""",
        ".*(Int).*"
      )
    }
  }

  describe(kindWithOvrd.toString) {

    it("1") {

      val w1 = kindWithOvrd.infoOf[Dummy[Int, String]]

      TypeVizShort[w1.Out].typeStr
        .shouldBe(
          s"""String("Int a b String")"""
        )
    }

    it(" ... implicitly") {

      val w1 = kindWithOvrd[Dummy[Int, String]].summonInfo

      TypeVizShort[w1.Out].typeStr
        .shouldBe(
          s"""String("Int a b String")"""
        )
    }

    it("2") {
      val w1 = kindWithOvrd.infoOf[SingletonName[nonFinalV.type]]
      TypeVizShort[w1.Out].typeStr
        .shouldBe(
          s"""String("shapesafe.m.viz.KindVizCTSpec.nonFinalV")"""
        )
    }

    describe("abort on error") {

      val localV: "a" = "a"

      it("1") {

        val w1 = kindWithOvrd.infoOf[SingletonName[localV.type]]
        TypeVizShort[w1.Out].typeStr
          .shouldBe(
            s"""String("FormatOvrd.SingletonName")"""
          )
      }

    }
  }
}

object KindVizCTSpec {

  val kindNoTree = KindVizCT.NoTree
  val kindWithOvrd = KindVizCT.WithOvrd

  final val finalV = "b"
  val nonFinalV = "b"

  case class Dummy[T1, T2]() extends (T1 ~~ SingletonName["a"] ~~ SingletonName[finalV.type] ~~ T2) {

//    final val infoOf = KindVizCT.infoOf[Dummy[T1, T2]]
  }

  object Dummy {
    lazy val name: String = classOf[Dummy[_, _]].getCanonicalName
  }
}
