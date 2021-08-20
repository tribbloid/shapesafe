package org.shapesafe.core.shape

import org.shapesafe.core.debugging.CanPeek
import org.shapesafe.core.shape.args.ApplyLiterals
import org.shapesafe.core.tuple._
import shapeless.Witness

import scala.language.implicitConversions

trait Names extends IndicesMagnet with Names.Proto.Tuple {}

object Names extends CanCons with ApplyLiterals.ToNames {

  type VBound = String

  object Proto extends StaticTuples.Total[VBound] with CanInfix_>< {}

  type Tuple = Names

  class Eye extends Proto.Eye with Names {
    override type AsIndices = Indices.Eye

    override def asIndices: Indices.Eye = Indices.Eye
  }
  lazy val Eye = new Eye

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

  implicit def consW[TAIL <: Tuple, HEAD <: String]: ConsLemma.FromFn2[TAIL, HEAD, TAIL >< HEAD] = {

    ConsLemma.from[TAIL, HEAD].to { (tail, head) =>
      new ><(tail, head)
    }
  }

  implicit class Infix[SELF <: Tuple](self: SELF) {

    def ><(name: Witness.Lt[String]): SELF >< name.T = {

      new ><(self, name.value)
    }
  }

  implicit def toEyeInfix(s: Names.type): Infix[Eye] = Infix(Eye)

  trait Syntax {

    implicit def literalToNames(v: String)(
        implicit
        w: Witness.Aux[v.type]
    ): Eye >< v.type = {

      Eye >< w
    }

    implicit def literalToInfix(v: String)(
        implicit
        w: Witness.Aux[v.type]
    ): Infix[Eye >< v.type] = {

      Infix(Eye >< w)
    }
  }

  object Syntax extends Syntax
}
