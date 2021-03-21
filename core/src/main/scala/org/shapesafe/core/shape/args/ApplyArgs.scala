package org.shapesafe.core.shape.args

import org.shapesafe.core.Poly1Base
import org.shapesafe.core.shape.Names
import shapeless.HList

trait ApplyArgs {

  type OUB
  val fromHList: Poly1Base[HList, OUB]

  type Result[T <: OUB]
  def toResult[T <: OUB](v: T): Result[T]
}

object ApplyArgs {

  trait Direct extends ApplyArgs {

    override type Result[T <: OUB] = T
    override def toResult[T <: OUB](v: T): T = v
  }
}
