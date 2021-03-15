package org.shapesafe.core.arity

import org.shapesafe.core.arity.LeafArity.Literal
import org.shapesafe.core.arity.ops.ArityOps
import org.shapesafe.core.axis.Axis.:<<-
import org.shapesafe.core.axis.{Axis, AxisMagnet}
import shapeless.Witness

import scala.language.implicitConversions
import scala.util.Try

trait Arity extends AxisMagnet {

  override lazy val toString: String = {

    valueStr + ":" + this.getClass.getSimpleName
  }

  def runtimeArity: Int

  final lazy val runtimeTry = Try(runtimeArity)
  final def runtimeOpt: Option[Int] = runtimeTry.toOption

  lazy val valueStr: String = runtimeTry
    .map(_.toString)
    .recover {
      case ee: Exception =>
        ee.getMessage
    }
    .get
}

object Arity {

  implicit def toOps[T <: Arity](v: T): ArityOps[T] = ArityOps[T](v)

  implicit class NameOps[T <: Arity](self: T) {

    def withNameT[S <: String](
        implicit
        name: Witness.Aux[S]
    ): T :<<- S = Axis(self, name)

    def withName(name: Witness.Lt[String]): T :<<- name.T = {

      :<<-(name)
    }

    def :<<-(name: Witness.Lt[String]): T :<<- name.T = withNameT(name)
  }

  def apply(w: Witness.Lt[Int]): Literal[w.T] = {
    Literal.apply(w)
  }

  // TODO: this is not working so far
  implicit def fromSInt[T <: Int with Singleton](v: T)(
      implicit
      toW: Witness.Lt[T]
  ): Literal[T] = {
    apply(toW).asInstanceOf[Literal[T]]
  }
}
