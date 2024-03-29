package shapesafe.core.axis

import ai.acyclic.prover.commons.same.Same
import shapeless.Witness
import shapeless.labelled.FieldType
import shapesafe.core.arity.{Arity, ArityType, ConstArity}
import shapesafe.core.debugging.{HasNotation, Notations}
import shapesafe.core.{XInt, XString}

import scala.language.implicitConversions

trait Axis extends AxisLike with Same.ByEquality.IWrapper with HasNotation {
  // TODO:; can be a subclass of shapeless KeyTag

  final type Field = FieldType[Name, _ArityType]
  final def asField: Field = arityType.asInstanceOf[Field]

  type _Axis >: this.type <: Axis
  final def axis: _Axis = this: _Axis

  final override protected lazy val samenessDelegatedTo: Any = (arityType, name)
}

object Axis {

  type ->>[N <: String, D] = FieldType[N, D]
  type UB_->> = (_ <: String) ->> ArityType

  // TODO: N can be eliminated
  final class :<<-[
      A <: ArityType,
      N <: XString
  ](
      val arityType: A,
      val nameW: Witness.Aux[N]
  ) extends Axis
      //      with KeyTag[N, D :<<- N]
      // TODO: remove? FieldType[] has some black magic written in macro
      {

    type _ArityType = A

    type _Axis = _ArityType :<<- Name

    trait NameAsNotation extends HasNotation {

      override type Notation = Name
    }

    override type Notation = Notations.:<<-[A#Notation, NameAsNotation#Notation]

    override lazy val toString: String = {
      if (name.isEmpty) s"$arityType"
      else s"$arityType :<<- $name"
    }
  }

  def apply(
      value: Arity,
      name: Witness.Lt[XString]
  ): :<<-[value._ArityType, name.T] = {

    new :<<-(value.arityType, name)
  }

  implicit def fromXInt[V <: XInt](v: V)(
      implicit
      toW: Witness.Aux[V]
  ): Arity.^[ConstArity.Literal[V]] = {

    Arity(toW)
  }
}
