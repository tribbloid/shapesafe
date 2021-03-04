package org.shapesafe.core.shape

import com.tribbloids.graph.commons.util.WideTyped
import com.tribbloids.graph.commons.util.viz.VizType
import org.scalatest.Ignore
import org.shapesafe.BaseSpec
import shapeless.ops.hlist.ZipWithKeys
import shapeless.{HList, HNil, SingletonProductArgs}

//https://github.com/milessabin/shapeless/issues/1072

@Ignore
class SingletonProductArgsSpike extends BaseSpec {

  import SingletonProductArgsSpike._

  it("a") {

    VizType[a.type].shouldBe()
    VizType[b.type].shouldBe()
  }
}

//@Ignore
object SingletonProductArgsSpike {

  val a: String with Singleton = "a"
  val b: String = "a"

  import shapeless.syntax.singleton._

  object SingletonBroker extends SingletonProductArgs {

    def applyProduct[H <: HList](v: H)(
        implicit
        withKeys: ZipWithKeys[H, H]
    ): withKeys.Out = {

      withKeys.apply(v)
    }
  }

  import shapeless.Witness._

  val v1 = SingletonBroker("a", 1) // <------------------------------------------------------works
//  val v2 = SingletonBroker.applyProduct("a".narrow :: HNil) //<-----------------------breaks!

  val k1 = WideTyped("a".narrow :: HNil)

  val vv1 = WideTyped("a" :: HNil)

//  VizType[ZipWithKeys[k1.Wide, vv1.Wide]].shouldBe()

//  implicitly[ZipWithKeys[k1.Wide, vv1.Wide]]

  val x = "b"
//    val v1 = SingletonBroker("a", x)

}
