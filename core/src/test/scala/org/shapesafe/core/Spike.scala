package org.shapesafe.core

import org.scalatest.Ignore
import org.shapesafe.BaseSpec
import shapeless.Witness

@Ignore
class Spike extends BaseSpec {}

object Spike {

  def adhocW = Witness("a")

  val singletonW = Witness("a")
}
