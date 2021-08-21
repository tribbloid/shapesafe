package org.shapesafe.core.axis

import org.shapesafe.graph.commons.util.IDMixin
import org.shapesafe.core.arity.{Arity, ArityAPI}
import org.shapesafe.core.debugging.Expressions.Expr
import org.shapesafe.core.debugging.{CanPeek, DebugUtil, Expressions, OpStrs}
import shapeless.Witness
import shapeless.labelled.FieldType

import scala.language.implicitConversions

trait Axis extends AxisLike with IDMixin with CanPeek {
  //TODO:; can be a subclass of shapeless KeyTag

  final type Field = FieldType[Name, _Arity]
  final def asField: Field = arity.asInstanceOf[Field]

  type _Axis >: this.type <: Axis
  final def axis: _Axis = this: _Axis

  override protected lazy val _id: Any = (arity, name)
}

object Axis {

  type ->>[N <: String, D] = FieldType[N, D]
  type UB_->> = (_ <: String) ->> Arity

  // TODO: N can be eliminated
  final class :<<-[
      A <: Arity,
      N <: String with Singleton
  ](
      val arity: A,
      val nameW: Witness.Aux[N]
  ) extends Axis
//      with KeyTag[N, D :<<- N]
      // TODO: remove? FieldType[] has some black magic written in macro
      {

    type _Arity = A

    type _Axis = _Arity :<<- Name

    trait CanPeekName extends CanPeek {

      override type _AsOpStr = Name
      override type _AsExpr = Name
    }

    type _AsOpStr = DebugUtil.Br[OpStrs.Infix[A, " :<<- ", CanPeekName]]
    override type _AsExpr = Expressions.:<<-[Expr[A], Expr[CanPeekName]]

    override lazy val toString: String = {
      if (name.isEmpty) s"$arity"
      else s"$arity :<<- $name"
    }
  }

  def apply(
      value: ArityAPI,
      name: Witness.Lt[String with Singleton]
  ): :<<-[value._Arity, name.T] = {

    new :<<-(value.arity, name)
  }
}
