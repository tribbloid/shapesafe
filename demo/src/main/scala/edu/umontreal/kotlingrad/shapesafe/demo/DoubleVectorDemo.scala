package edu.umontreal.kotlingrad.shapesafe.demo

import edu.umontreal.kotlingrad.shapesafe.tensor.DoubleVector

object DoubleVectorDemo {

  def main(args: Array[String]): Unit = {

    val x3 = DoubleVector(1.0, 2.0, 3.0)
    val x50 = DoubleVector(
      1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 0.0, //
      1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 0.0, //
      1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 0.0, //
      1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 0.0, //
      1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 0.0, //
    )

    val y3 = DoubleVector.random(3)
    val y6 = DoubleVector.random(6)
    val y50 = DoubleVector.random(50)
    val y53 = DoubleVector.random(53)

    println(x3 dot_* y3)

//    x3 dot_* y4  doesn't compile

    println((y3 concat y50) dot_* y53)

    println((x50 concat y3) dot_* y53)

//    (x50 concat y3) dot_* y6  doesn't compile
  }
}
