package edu.umontreal.kotlingrad.shapesafe.demo

import edu.umontreal.kotlingrad.shapesafe.tensor.DoubleVector

object DoubleVectorDemo {

  def main(args: Array[String]): Unit = {

    val x3 = DoubleVector(1.0, 2.0, 3.0)
    val y3 = DoubleVector.random(3)
    val y4 = DoubleVector.random(4)

    println(x3 dot_* y3)

//    x3 dot_* y4  doesn't compile
  }
}
