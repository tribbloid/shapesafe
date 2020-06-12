package edu.umontreal.kotlingrad.shapesafe

import org.scalatest.FunSpec
import shapeless.test.illTyped

abstract class BaseSpec extends FunSpec {

  /**
    * ScalaTest assertDoesNotCompile sometimes malfunction (due to unwashed execution order?)
    * & doesn't perform literal check
    * if the code compiles successfully, the project compilation will fail
    */
  val shouldNotCompile: illTyped.type = illTyped
}
