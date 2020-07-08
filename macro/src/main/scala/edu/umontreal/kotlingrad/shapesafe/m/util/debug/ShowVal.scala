package edu.umontreal.kotlingrad.shapesafe.m.util.debug

import edu.umontreal.kotlingrad.shapesafe.m.util.TypeTag

case class ShowVal[T](
    value: T,
    inferredTTag: TypeTag[_],
    runtimeClass: Class[_]
) {

  override def toString: String = {
    s"""$value
         | : ${inferredTTag.tpe}
         | @ ${runtimeClass.getCanonicalName}
         """.trim.stripMargin
  }

  lazy val inferType: ShowType = {

    ShowType.apply(inferredTTag)
  }
}
