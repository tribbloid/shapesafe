package edu.umontreal.kotlingrad.shapesafe.m.util

case class InferredTypeOf[TT](value: TT) {

  type T = TT
}
