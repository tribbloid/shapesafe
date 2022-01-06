package shapesafe.core.axis

import shapesafe.core.XString
import shapesafe.core.arity.{Arity, ArityAPI}
import shapesafe.core.debugging.{CanPeek, Expressions}
import ai.acyclic.graph.commons.IDMixin
import shapeless.Witness
import shapeless.labelled.FieldType

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
      N <: XString
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

      override type Expr = Name
    }

    override type Expr = Expressions.:<<-[A#Expr, CanPeekName#Expr]

    override lazy val toString: String = {
      if (name.isEmpty) s"$arity"
      else s"$arity :<<- $name"
    }
  }

  def apply(
      value: ArityAPI,
      name: Witness.Lt[XString]
  ): :<<-[value._Arity, name.T] = {

    new :<<-(value.arity, name)
  }
}
