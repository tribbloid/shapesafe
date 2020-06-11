package edu.umontreal.kotlingrad.shapesafe.demo

import edu.umontreal.kotlingrad.shapesafe.util.KotlinHelper

// TODO: remove, not compatible
fun main() {

    run {
        val w = KotlinHelper.witnessOf(3)
        println(w.javaClass)
    }

    run {
        val w = KotlinHelper.witnessOf(4)
        println(w.javaClass)
    }

    run {
        val w = KotlinHelper.witnessOf(5)
        println(w.javaClass)
    }

    run {
        val w = KotlinHelper.witnessOf(3)
        println(w.javaClass)
    }


//    val w = Witness.witness0()

//    val x3 = DoubleVector(1.0, 2.0, 3.0)
//
//    val y3 = DoubleVector.random(3)

}