package org.shapesafe.core.shape

import com.tribbloids.graph.commons.util.reflect.format.FormatOvrd.Only
import org.shapesafe.core.debugging.CanPeek
import org.shapesafe.core.debugging.DebuggingUtil.StrOrRaw
import org.shapesafe.core.shape.args.ApplyLiterals
import org.shapesafe.core.tuple._
import shapeless.Witness

import scala.language.implicitConversions

trait Names extends IndicesMagnet with Names.proto.Tuple {}

object Names extends TupleSystem with CanCons with CanFromLiterals with ApplyLiterals.ToNames {

  type UpperBound = String

  object proto extends StaticTuples.Total[UpperBound] with CanInfix_>< {}

  type Tuple = Names

  class Eye extends proto.Eye with Names {
    override type AsIndices = Indices.Eye

    override def asIndices: Indices.Eye = Indices.Eye
  }
  lazy val Eye = new Eye

  class ><[
      TAIL <: Tuple,
      HEAD <: UpperBound
  ](
      override val tail: TAIL,
      override val head: HEAD
  ) extends proto.><[TAIL, HEAD](tail, head)
      with Tuple {

    val headW: Witness.Aux[HEAD] = Witness[HEAD](head).asInstanceOf[Witness.Aux[HEAD]]

    override type AsIndices = Indices.><[tail.AsIndices, Index.Name[HEAD]]

    override def asIndices: AsIndices =
      tail.asIndices >< Index.Name(headW)

    trait PeekHead extends CanPeek {
      override type _OpStr = Head
      override type _Expression = Head
    }
  }

  implicit def consW[TAIL <: Tuple, HEAD <: String]: Cons.FromFn2[TAIL, HEAD, TAIL >< HEAD] = {

    Cons.from[TAIL, HEAD].to { (tail, head) =>
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

  val i = Names("i")
  val ij = Names("i", "j")
  val jk = Names("j", "k")
  val ik = Names("i", "k")
}
