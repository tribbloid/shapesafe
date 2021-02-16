package org.shapesafe.core.arity

import org.shapesafe.core.arity.LeafArity.Literal
import org.shapesafe.core.arity.ProveArity.~~>
import org.shapesafe.core.axis.Axis
import shapeless.Witness

import scala.language.implicitConversions
import scala.util.Try

trait Arity {

  final def verify[
      SELF >: this.type <: Arity,
      O <: Arity
  ](
      implicit
      prove: SELF ~~> O
  ): O = prove.apply(this).out

  final def proveLeaf[
      SELF >: this.type <: Arity,
      O <: LeafArity
  ](
      implicit
      prove: SELF ~~> O
  ): O = prove.apply(this).out

  final override def toString: String = {

    valueStr + ":" + this.getClass.getSimpleName
  }

  def runtime: Int

  final lazy val runtimeTry = Try(runtime)
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
}
