package com.tribbloids.shapesafe.m.arity

import com.tribbloids.shapesafe.m.~~>
import shapeless.Witness
import shapeless.Witness.Lt
import shapeless.labelled.FieldType
import shapeless.syntax.SingletonOps

// for "Expression"
trait Expression {

  final def proveArity[
      T >: this.type <: Expression,
      R <: ProofOfArity
  ](implicit prove: T ~~> R): R = prove.apply(this)

  final def proveArity_generic[
      T >: this.type <: Expression
  ](
      implicit prove: T ~~> ProofOfArity
  ): ProofOfArity = prove.apply(this)

  lazy val valueStr: String = "[??]"

  final override def toString: String = {

    this.getClass.getSimpleName + ": " + valueStr
  }

}

object Expression {

  object Unprovable extends Expression

  implicit class ExpressionOps[T <: Expression](self: T) {

    def withName(name: Witness.Lt[String]): FieldType[name.T, T] = {

      self.asInstanceOf[FieldType[name.T, T]]
    }

    def <<-(name: Witness.Lt[String]): FieldType[name.T, T] = withName(name)
  }

//  case class WitnessAsSingleton[_T](v: Witness.Lt[_T]) extends SingletonOps {
//    override type T = v.T
//    override val witness: Witness.Aux[v.T] = v
//  }
}
