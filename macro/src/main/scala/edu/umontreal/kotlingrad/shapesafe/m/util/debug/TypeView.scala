package edu.umontreal.kotlingrad.shapesafe.m.util.debug

import edu.umontreal.kotlingrad.shapesafe.m.util.ScalaReflection.universe
import edu.umontreal.kotlingrad.shapesafe.m.util.TypeTag
import edu.umontreal.kotlingrad.shapesafe.m.util.debug.DebugUtils.InvokeRef

case class TypeView(tpe: universe.Type)(
    implicit
    val invokeRef: InvokeRef = InvokeRef(exclude = Seq(classOf[TypeView]))
) extends HasInvokeRef {

  import universe._

  lazy val baseTypes: List[Type] = {

    val baseClzs = tpe.baseClasses

    val result = baseClzs.map { clz =>
      tpe.baseType(clz)
    }
    result
  }

  override def toString: String = {

    s"""
       |$tpe
       |${baseTypes.map(v => "\t- " + v).mkString("\n")}
       """.trim.stripMargin
  }
}

object TypeView {

  def apply[T](implicit ttag: TypeTag[T]): TypeView = TypeView(ttag.tpe)
}
