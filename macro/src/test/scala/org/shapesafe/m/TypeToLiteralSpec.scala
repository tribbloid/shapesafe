package org.shapesafe.m

import com.tribbloids.graph.commons.testlib.BaseSpec
import com.tribbloids.graph.commons.util.reflect.Reflection
import com.tribbloids.graph.commons.util.reflect.format.TypeFormat
import com.tribbloids.graph.commons.util.viz.TypeViz
import shapeless.Witness

class TypeToLiteralSpec extends BaseSpec {

  import TypeToLiteralSpec._

  val viz: TypeViz[Reflection.Runtime.type] = TypeViz.withFormat {
    import TypeFormat._

    Type ~ DeAlias ~ HidePackages
  }

  //  it("spike") {
  //
  //    val tt = Witness.`abc`
  //    type T = Witness.`abc`
  //
  //    TypeViz.infer(tt).should_=:=()
  //    TypeViz[T].should_=:=()
  //  }

  import TypeToLiteral._

  describe(Kind.productPrefix) {

    val gd = viz.infer(Witness("Int").value)

    it("ground truth") {

      gd.typeStr.shouldBe(
        """String("Int")"""
      )
    }

    it("Int") {

      val w1 = TypeToLiteral.Kind[Int]
      viz[w1.Out].should_=:=(gd)
    }

    it(" ... implicitly") {

      val w1 = TypeToLiteral.Kind.From[Int]().summon

      //      viz.infer(w1).should_=:=()
      viz[w1.Out].should_=:=(gd)
    }

    it("generic 1") {

      val w1 = TypeToLiteral.Kind[Dummy[_, _]]

      viz[w1.Out].typeStr
        .shouldBe(
          s"""String("GetInfoOfSpec.Dummy")"""
        )
    }

    it("generic 2") {

      val e1 = Dummy[Int, String]()

      viz[e1.KindOf.nameOfSelf.type#Out].typeStr
        .shouldBe(
          s"""String("GetInfoOfSpec.Dummy")"""
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
}

object TypeToLiteralSpec {

  case class Dummy[T1, T2]() {

    object KindOf {

      final val nameOfT1 = TypeToLiteral.Kind[T1]

      final val nameOfSelf = TypeToLiteral.Kind[Dummy[T1, T2]]
    }
  }

  object Dummy {
    lazy val name: String = classOf[Dummy[_, _]].getCanonicalName
  }
}
