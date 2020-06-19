package edu.umontreal.kotlingrad.shapesafe.m

package object util {

  type TypeTag[T] = ScalaReflection.universe.TypeTag[T]
}
