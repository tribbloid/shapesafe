package edu.umontreal.kotlingrad.shapesafe.common

package object util {

  type TypeTag[T] = ScalaReflection.universe.TypeTag[T]
}
