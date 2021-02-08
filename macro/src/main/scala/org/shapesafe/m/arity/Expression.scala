package org.shapesafe.m.arity

import org.shapesafe.m.arity.ProveArity.~~>
import org.shapesafe.m.axis.{Axis, NameWide}
import shapeless.Witness

import scala.language.implicitConversions

// for "Expression"
trait Expression {

  final def proveArity[
      T >: this.type <: Expression,
      R <: ProveArity.Proof
  ](implicit prove: T ~~> R): R = prove.apply(this)

  final def proveArity_generic[
      T >: this.type <: Expression
  ](
      implicit prove: T ~~> ProveArity.Proof
  ): ProveArity.Proof = prove.apply(this)

  lazy val valueStr: String = "[??]"

  final override def toString: String = {

    valueStr + ":" + this.getClass.getSimpleName
  }

}

object Expression {

  object Unprovable extends Expression

  implicit class ExpressionOps[T <: Expression](self: T) {

    def withNameT(implicit name: Witness.Lt[String]) = Axis(self, name)

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
