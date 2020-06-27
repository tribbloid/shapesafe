package edu.umontreal.kotlingrad.shapesafe.m.util.debug

import edu.umontreal.kotlingrad.shapesafe.m.util.debug.DebugUtils.InvokeRef

trait HasInvokeRef {

  def invokeRef: InvokeRef

  def print(): Unit = {

    println(
      s"""
         |${this.toString}
         |\tat ${invokeRef.showStr}
         """.stripMargin
    )
  }
}
