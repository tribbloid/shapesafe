package edu.umontreal.kotlingrad.shapesafe.common

import singleton.ops.impl.Op

package object arity {

  type IntOp = Op { type Out <: Int }
}
