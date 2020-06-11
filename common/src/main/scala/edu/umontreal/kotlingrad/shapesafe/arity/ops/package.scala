package edu.umontreal.kotlingrad.shapesafe.arity

package object ops {

  type ??[A1 <: Arity, A2 <: Arity] = BinaryUnsafe[A1, A2]
}
