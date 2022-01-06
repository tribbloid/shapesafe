package shapesafe.core.shape

import ai.acyclic.graph.commons.testlib.BaseSpec
import org.scalatest.Ignore
import shapeless.Witness

@Ignore
class SingletonConstraintSpike extends BaseSpec {

  import SingletonConstraintSpike._

  it("1") {

    val v = adhocW.value
    s1[v.type]()

    val a = "a"
    s1[a.type]()
  }

  it("2") {

//    s2[Witness._1] // breaks

    val a = "a"
    s2[a.type]()
  }

  it("3") {

    val v = singletonW.value

//    s3(v) // breaks
  }

  it("4") {

    type VT = singletonW.T
    val v: VT = singletonW.value

    s3(v)
  }
}

object SingletonConstraintSpike {

  def s1[T]()(
      implicit
      ev: Witness.Aux[T]
  ) = {}

  def s2[T]()(
      implicit
      ev: Witness.Lt[T]
  ) = {}

  def s3[T](v: T)(
      implicit
      ev: Witness.Aux[T]
  ) = {}

  def adhocW = Witness(3)

  val singletonW = Witness(3)

}
