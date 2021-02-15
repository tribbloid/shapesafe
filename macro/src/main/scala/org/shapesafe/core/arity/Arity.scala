package org.shapesafe.core.arity

import org.shapesafe.core.arity.ProveArity.=>>
import org.shapesafe.core.axis.{Axis, NameWide}
import shapeless.Witness

import scala.language.implicitConversions
import scala.util.Try

trait Arity {

  final def proveLeaf[
      SELF >: this.type <: Arity,
      O <: ProveArity.Proof.Lt[LeafArity]
  ](
      implicit
      prove: SELF =>> O
  ): O = prove.apply(this)

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

    def withNameT(
        implicit
        name: Witness.Lt[String]
    ) = Axis(self, name)

    def withName(name: Witness.Lt[NameWide]) = {

      :<<-(name)
    }

    def :<<-(name: Witness.Lt[NameWide]) = withNameT(name)
  }
  // TODO: this can be simplified by writing 1 function to cast Expression to FieldType
  //  or type lambda in dotty
//  trait AsNamed[I <: Expression] {
//
//    type Out <: Named
//
//    def apply(v: I): Out
//  }
//
//  trait AsNamed_Imp0 {
//
//    implicit def _nameless[I <: Expression]: Nameless[I] = new Nameless[I]
//
//    class Nameless[I <: Expression] extends AsNamed[I] {
//
//      type Out = FieldType[nameless.T, I]
//
//      override def apply(v: I): FieldType[nameless.T, I] = v <<- nameless // v <<- nameless
//    }
//  }
//
//  object AsNamed extends AsNamed_Imp0 {
//
//    implicit def _passThrough[Base <: Expression]: PassThrough[Base] = new PassThrough[Base]
//
//    class PassThrough[Base <: Expression] extends AsNamed[FieldType[_ <: String, Base]] {
//
//      type Out = FieldType[_ <: String, Base]
//
//      override def apply(v: FieldType[_ <: String, Base]): FieldType[_ <: String, Base] = v
//    }
//  }
//  case class WitnessAsSingleton[_T](v: Witness.Lt[_T]) extends SingletonOps {
//    override type T = v.T
//    override val witness: Witness.Aux[v.T] = v
//  }
}
