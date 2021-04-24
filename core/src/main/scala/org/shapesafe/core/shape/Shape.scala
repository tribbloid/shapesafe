package org.shapesafe.core.shape

import com.tribbloids.graph.commons.util.{TreeFormat, TreeLike}
import org.shapesafe.core.debugging.CanPeek
import org.shapesafe.core.shape.LeafShape.Eye
import org.shapesafe.core.shape.args.{ApplyLiterals, ApplyNats}

import scala.language.implicitConversions

trait Shape extends TreeLike with CanPeek {

  override lazy val treeFormat: TreeFormat = TreeFormat.Indent2Minimal

}

object Shape extends ApplyLiterals.ToShape with ShapeAPI {

  def box[T <: Shape](self: T): ShapeAPI.^[T] = ShapeAPI.^(self)

  implicit class Converters[S <: Shape](self: S) {

    def ^ : ShapeAPI.^[S] = ShapeAPI.^(self)
  }

  object Nats extends ApplyNats.ToShape {}

  object Literals extends ApplyLiterals.ToShape {}

  override type _Shape = LeafShape.Eye
  override def shape: Eye = LeafShape.Eye
}
