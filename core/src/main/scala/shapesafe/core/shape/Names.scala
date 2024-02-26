package shapesafe.core.shape

import ai.acyclic.prover.commons.tuple._
import shapeless.Witness
import shapesafe.core.debugging.HasNotation
import shapesafe.core.shape.args.ApplyLiterals
import shapesafe.core.{SymbolicTuples, XString}

import scala.language.implicitConversions

trait Names extends IndicesMagnet with Names.Backbone.Tuple {}

object Names extends Tuples with ApplyLiterals.ToNames {

  type VBound = XString // expand to take all String for gradual typing?

  object Backbone extends SymbolicTuples[VBound] {}

  type Tuple = Names

  class Eye extends Backbone.Eye with Names {
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
  ) extends Backbone.><[TAIL, HEAD](tail, head)
      with Tuple {

    val headW: Witness.Aux[HEAD] = Witness[HEAD](head).asInstanceOf[Witness.Aux[HEAD]]

    override type AsIndices = Indices.><[tail.AsIndices, Index.Name[HEAD]]

    override def asIndices: AsIndices = {
      import Indices._
      tail.asIndices >< Index.Name(headW)
    }

    trait PeekHead extends HasNotation {
      override type Notation = Head
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

  val i = Names("i")
  val ii = Names("i", "i")

  val ij = Names("i", "j")
  val jk = Names("j", "k")
  val ijjk = Names("i", "j", "j", "k")

  val ik = Names("i", "k")
}
