package org.shapesafe.core.arity

import org.shapesafe.core.arity.LeafArity.Literal
import org.shapesafe.core.arity.ProveArity.~~>
import org.shapesafe.core.arity.ops.ArityOps
import org.shapesafe.core.axis.Axis
import shapeless.Witness

import scala.language.implicitConversions
import scala.util.Try

trait Arity extends Axis.Nameless {

  final def verify[
      SELF >: this.type <: Arity,
      O <: Arity
  ](
      implicit
      prove: SELF ~~> O
  ): O = prove.apply(this).value

  final def eval[
      SELF >: this.type <: Arity,
      O <: LeafArity
  ](
      implicit
      prove: SELF ~~> O
  ): O = prove.apply(this).value

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

  override type Dimension = this.type
  override def dimension: this.type = this
}

object Arity {

  implicit class NameOps[T <: Arity](self: T) {

    def withNameT[S <: String](
        implicit
        name: Witness.Aux[S]
    ) = Axis(self, name)

    def withName(name: Witness.Lt[String]) = {

      :<<-(name)
    }

    def :<<-(name: Witness.Lt[String]) = withNameT(name)
  }

  def apply(w: Witness.Lt[Int]): Literal[w.T] = {
    Literal.apply(w)
  }

  implicit def toOps[T <: Arity](v: T): ArityOps[T] = ArityOps[T](v)

  implicit def nameless[V <: Arity](self: V) = Axis(self, Axis.emptyName)
}
