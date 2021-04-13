package org.shapesafe.m

import com.tribbloids.graph.commons.testlib.BaseSpec
import com.tribbloids.graph.commons.util.reflect.format.InfoFormat.ConstV
import com.tribbloids.graph.commons.util.reflect.format.TypeFormat
import com.tribbloids.graph.commons.util.viz.TypeViz
import org.shapesafe.m.FormattedTypeAsConst.{Kind, Ovrd}
import shapeless.Witness

class FormattedTypeAsConstSpec extends BaseSpec {

  import FormattedTypeAsConstSpec._

  val viz = TypeViz.withFormat(
    TypeFormat.Type +> TypeFormat.DeAlias +> TypeFormat.HidePackages
  )

  //  it("spike") {
  //
  //    val tt = Witness.`abc`
  //    type T = Witness.`abc`
  //
  //    TypeViz.infer(tt).should_=:=()
  //    TypeViz[T].should_=:=()
  //  }

  describe(Kind.productPrefix) {

    val gd = viz.infer(Witness("Int").value)

    it("ground truth") {

      gd.typeStr.shouldBe(
        """String("Int")"""
      )
    }

    it("Int") {

      val w1 = FormattedTypeAsConst.Kind[Int]
      viz[w1.Out].should_=:=(gd)
    }

    it(" ... implicitly") {

      val w1 = FormattedTypeAsConst.Kind.From[Int]().summon

//      viz.infer(w1).should_=:=()
      viz[w1.Out].should_=:=(gd)
    }

    it("generic 1") {

      val w1 = FormattedTypeAsConst.Kind[Dummy[_, _]]

      viz[w1.Out].typeStr
        .shouldBe(
          s"""String("FormattedTypeAsConstSpec.Dummy")"""
        )
    }

    it("generic 2") {

      val e1 = Dummy[Int, String]()

      viz[e1.KindOf.nameOfSelf.type#Out].typeStr
        .shouldBe(
          s"""String("FormattedTypeAsConstSpec.Dummy")"""
        )
    }

//     TODO: doesn't work
//    it("generic type arg") {
//
//      val e1 = Dummy[Int, String]()
//
//      viz[e1.KindOf.nameOfT1.type#Out].typeStr
//        .shouldBe(
//          s"""String("${Dummy.name}")"""
//        )
//    }
  }

  // TODO: doesn't work
  describe(Ovrd.productPrefix) {

    it("Int") {

      val w1 = FormattedTypeAsConst.Ovrd[ConstV[123]]
      viz[w1.Out].should_=:=()
    }

    it(" ... implicitly") {

      val w1 = FormattedTypeAsConst.Kind.From[ConstV[123]]().summon

      //      viz.infer(w1).should_=:=()
      viz[w1.Out].should_=:=()
    }
  }
}

object FormattedTypeAsConstSpec {

  case class Dummy[T1, T2]() {

    object KindOf {

      final val nameOfT1 = FormattedTypeAsConst.Kind[T1]

      final val nameOfSelf = FormattedTypeAsConst.Kind[Dummy[T1, T2]]
    }
  }

  object Dummy {
    lazy val name: String = classOf[Dummy[_, _]].getCanonicalName
  }
}
