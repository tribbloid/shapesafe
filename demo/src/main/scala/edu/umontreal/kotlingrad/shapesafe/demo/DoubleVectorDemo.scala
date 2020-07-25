package edu.umontreal.kotlingrad.shapesafe.demo

import edu.umontreal.kotlingrad.shapesafe.core.tensor.{DoubleVector, Shape}
import edu.umontreal.kotlingrad.shapesafe.m.arity.binary.MayEqual
import edu.umontreal.kotlingrad.shapesafe.m.arity.{Arity, Proof}
import edu.umontreal.kotlingrad.shapesafe.m.~~>

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

    {
      val conved = x50.conv(x3)
      println(conved.data.size)

      conved.arity.internal.requireEqual(48)
      //    conved.arity.internal.requireEqual(49) // doesn't compile
    }

    {
      val conved = x50.pad(5).conv(x3, 2)
      println(conved.data.size)

      conved.arity.internal.requireEqual(29)
      //    conved.arity.internal.requireEqual(28) // doesn't compile
    }

    val _3 = Arity(3)

    def printD3[A <: Shape](v: DoubleVector[A])(
      implicit
      proofOfArity: A MayEqual _3.Out ~~> Proof
    ): Unit = {

      println(v)
    }

    printD3(DoubleVector.random(3))

//    printD3(DoubleVector.random(4)) // doesn't compile


  }
}
