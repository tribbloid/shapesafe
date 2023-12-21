package shapesafe.core.shape.args

import shapesafe.core.AdHocPoly1

trait ApplyArgs {

  type OUB
  val fromHList: AdHocPoly1

  type Result[T <: OUB]
  def toResult[T <: OUB](v: T): Result[T]
}

object ApplyArgs {

  trait Direct extends ApplyArgs {

    override type Result[T <: OUB] = T
    override def toResult[T <: OUB](v: T): T = v
  }
}
