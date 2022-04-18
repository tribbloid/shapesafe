package shapesafe.core.shape.args

import shapesafe.core.AdHocPoly1
import shapeless.HList

trait ApplyArgs {

  type OUB
  val fromHList: AdHocPoly1[HList, OUB]

  type Result[T <: OUB]
  def toResult[T <: OUB](v: T): Result[T]
}

object ApplyArgs {

  trait Direct extends ApplyArgs {

    override type Result[T <: OUB] = T
    override def toResult[T <: OUB](v: T): T = v
  }
}
