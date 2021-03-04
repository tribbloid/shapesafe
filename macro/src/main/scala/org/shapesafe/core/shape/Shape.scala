package org.shapesafe.core.shape

import com.tribbloids.graph.commons.util.{TreeFormat, TreeLike}
import org.shapesafe.core.axis.Axis
import org.shapesafe.core.shape.ProveShape.|--
import org.shapesafe.core.shape.ops.{LeafShapeOps, ShapeOps}
import org.shapesafe.core.tuple.{ApplyLiterals, ApplyNats}

import scala.language.implicitConversions

trait Shape extends TreeLike {

  override lazy val format: TreeFormat = TreeFormat.Indent2Minimal

  final def verify[
      SELF >: this.type <: Shape,
      O <: Shape
  ](
      implicit
      prove: SELF |-- O
  ): O = prove.apply(this).value

  final def simplify[
      SELF >: this.type <: Shape,
      O <: LeafShape
  ](
      implicit
      prove: SELF |-- O
  ): O = prove.apply(this).value

  final def eval[ // TODO: eval each member!
      SELF >: this.type <: Shape,
      O <: LeafShape
  ](
      implicit
      prove: SELF |-- O
  ): O = simplify(prove)
}

object Shape extends ApplyLiterals {

  import LeafShape._

  implicit def toEyeOps(v: this.type): LeafShapeOps[Eye] = LeafShape.toOps[Eye](Eye)

  implicit def toOps[T <: Shape](self: T): ShapeOps[T] = new ShapeOps(self)

  type Vector[T <: Axis] = Eye >< T

  type Matrix[T1 <: Axis, T2 <: Axis] = Eye >< T1 >< T2

  object Nats extends ApplyNats {

    override val fromHList: LeafShape.FromNats.type = LeafShape.FromNats
  }

  object Literals extends ApplyLiterals {

    override val fromHList: LeafShape.FromLiterals.type = LeafShape.FromLiterals
  }

  override val fromHList: LeafShape.FromLiterals.type = LeafShape.FromLiterals
}
