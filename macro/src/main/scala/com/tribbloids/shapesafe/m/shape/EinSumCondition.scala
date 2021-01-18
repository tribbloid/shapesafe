package com.tribbloids.shapesafe.m.shape

import com.tribbloids.shapesafe.m.Poly1Group
import com.tribbloids.shapesafe.m.arity.binary.AssertEqual
import com.tribbloids.shapesafe.m.arity.{Arity, Expression}
import shapeless.labelled.FieldType
import shapeless.ops.record.{Keys, Selector}
import shapeless.{::, HList, NotContainsConstraint, Witness}

object EinSumCondition extends Poly1Group[(HList, FieldType[_ <: String, Expression]), HList] {

  import com.tribbloids.shapesafe.m.arity.OfArity._

  implicit def IfNewName[
      OLD <: HList,
      N <: String,
      D <: Expression,
      OLDNS <: HList
  ](
      implicit
      name: Witness.Aux[N],
      keys: Keys.Aux[OLD, OLDNS],
      NotContainsConstraint: NotContainsConstraint[OLDNS, N]
  ): ==>[(OLD, FieldType[N, D]), FieldType[N, D] :: OLD] = {

    case (old, field) =>
      field.asInstanceOf[FieldType[N, D]] :: old

  }

//  implicit def condition[
//      OLD <: HList,
//      N <: String,
//      D1 <: Expression,
//      D2 <: Expression,
//      O <: Arity
//  ](
//      implicit
//      name: Witness.Aux[N],
//      selector: Selector.Aux[OLD, N, D1],
//      lemma: AssertEqual[D1, D2] ~~> Proof.Aux[O]
//  ): ==>[(OLD, FieldType[N, D2]), FieldType[N, O] :: OLD] = {
//
//    case (old, field) =>
//      import shapeless.record._
//
//      val d1 = old.apply(name)
//      val d2 = field: D2
//
//      val d_new: O = lemma.apply(AssertEqual(d1, d2)).out
//
//      d_new.asInstanceOf[FieldType[N, O]] :: old
//  }

  implicit def IfExistingName[
      OLD <: HList,
      N <: String,
      D1 <: Expression,
      D2 <: Expression,
      O <: Arity
  ](
      implicit
      name: Witness.Aux[N],
      selector: Selector.Aux[OLD, N, D1],
      lemma: AssertEqual[D1, D2] ~~> Proof.Aux[O]
  ): ==>[(OLD, FieldType[N, D2]), FieldType[N, O] :: OLD] = {

    case (old, field) =>
      import shapeless.record._

      val d1 = old.apply(name)
      val d2 = field

      val d_new: O = lemma.apply(AssertEqual(d1, d2)).out

      d_new.asInstanceOf[FieldType[N, O]] :: old
  }

}
