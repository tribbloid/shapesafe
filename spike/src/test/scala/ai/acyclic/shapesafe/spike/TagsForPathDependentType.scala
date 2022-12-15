package ai.acyclic.shapesafe.spike

import ai.acyclic.prover.commons.testlib.BaseSpec

import scala.reflect.ClassTag
import scala.reflect.runtime.universe

class TagsForPathDependentType extends BaseSpec {

  import universe._

  def infer(): Unit = {
    type U = (Int, String)

    implicitly[TypeTag[(Int, String)]]

    shouldNotCompile(
      "implicitly[TypeTag[U]]"
    )

    implicitly[ClassTag[U]]
  }
}
