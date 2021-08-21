package org.shapesafe.core.shape

import org.shapesafe.core.XString
import org.shapesafe.core.debugging.CanPeek
import org.shapesafe.core.shape.args.ApplyLiterals
import org.shapesafe.core.tuple._
import shapeless.Witness

import scala.language.implicitConversions

trait Names extends IndicesMagnet with Names.Proto.Tuple {}

object Names extends Tuples with ApplyLiterals.ToNames {

  type VBound = XString // expand to take all String for gradual typing?

  object Proto extends StaticTuples[VBound] {}

  type Tuple = Names

  class Eye extends Proto.Eye with Names {
    override type AsIndices = Indices.Eye

    override def asIndices: Indices.Eye = Indices.Eye
  }
  val Eye = new Eye

  class ><[
      TAIL <: Tuple,
      HEAD <: VBound
  ](
      override val tail: TAIL,
      override val head: HEAD
  ) extends Proto.><[TAIL, HEAD](tail, head)
      with Tuple {

    val headW: Witness.Aux[HEAD] = Witness[HEAD](head).asInstanceOf[Witness.Aux[HEAD]]

    override type AsIndices = Indices.><[tail.AsIndices, Index.Name[HEAD]]

    override def asIndices: AsIndices =
      tail.asIndices >< Index.Name(headW)

    trait PeekHead extends CanPeek {
      override type _AsOpStr = Head
      override type _AsExpr = Head
    }
  }

  override def cons[TAIL <: Tuple, HEAD <: VBound](
      tail: TAIL,
      head: HEAD
  ): TAIL >< HEAD =
    new ><(tail, head)

  trait Syntax {

    implicit def literalToNames(v: String)(
        implicit
        w: Witness.Aux[v.type]
    ): Eye >< v.type = {

      Eye >< w.value
    }

    implicit def literalToExtension(v: String)(
        implicit
        w: Witness.Aux[v.type]
    ): tupleExtension[Eye >< v.type] = {

      tupleExtension(Eye >< w.value)
    }
  }

  object Syntax extends Syntax
}
