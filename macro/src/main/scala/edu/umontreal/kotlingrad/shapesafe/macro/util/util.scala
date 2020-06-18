package edu.umontreal.kotlingrad.shapesafe.`macro`

package object util {

  type TypeTag[T] = ScalaReflection.universe.TypeTag[T]
}
