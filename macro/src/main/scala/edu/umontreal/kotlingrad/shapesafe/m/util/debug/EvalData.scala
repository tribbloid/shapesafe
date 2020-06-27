package edu.umontreal.kotlingrad.shapesafe.m.util.debug

import edu.umontreal.kotlingrad.shapesafe.m.util.ScalaReflection.universe
import edu.umontreal.kotlingrad.shapesafe.m.util.debug.DebugUtils.InvokeRef

case class EvalData[T](
    value: T,
    inferredTTag: universe.TypeTag[_],
    runtimeClass: Class[_]
)(
    implicit
    val invokeRef: InvokeRef = InvokeRef(exclude = Seq(classOf[EvalData[_]]))
) extends HasInvokeRef {

  override def toString: String = {
    s"""$value
         | : ${inferredTTag.tpe}
         | @ ${runtimeClass.getCanonicalName}
         """.trim.stripMargin
  }

  def printInferredType(): Unit = {

    val s = s"""
                 |${TypeView(inferredTTag.tpe)}
                 |\tat ${invokeRef.showStr}
         """.stripMargin

    println(s)
  }
}
