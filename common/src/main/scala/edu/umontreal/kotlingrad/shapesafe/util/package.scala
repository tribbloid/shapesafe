package edu.umontreal.kotlingrad.shapesafe

package object util {

  type TypeTag[T] = ScalaReflection.universe.TypeTag[T]
}
