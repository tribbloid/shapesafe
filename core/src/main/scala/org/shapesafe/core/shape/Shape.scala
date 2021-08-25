package org.shapesafe.core.shape

import org.shapesafe.core.debugging.CanPeek
import org.shapesafe.core.shape.StaticShape.Eye
import org.shapesafe.core.shape.args.{ApplyLiterals, ApplyNats}

trait Shape extends CanPeek {}

object Shape extends ApplyLiterals.ToShape with ShapeAPI {

  val Unchecked: ShapeAPI.^[org.shapesafe.core.shape.Unchecked.type] = org.shapesafe.core.shape.Unchecked.^

  def box[T <: Shape](self: T): ShapeAPI.^[T] = ShapeAPI.^(self)

  implicit class converters[S <: Shape](self: S) {

    def ^ : ShapeAPI.^[S] = ShapeAPI.^(self)
  }

  object Nats extends ApplyNats.ToShape {}

  object Literals extends ApplyLiterals.ToShape {}

  override type _Shape = StaticShape.Eye
  override def shape: Eye = StaticShape.Eye
}
