package org.shapesafe.core.shape

import org.shapesafe.core.{XInt, XString}
import org.shapesafe.core.arity.Utils.NatAsOp
import org.shapesafe.core.arity.{Arity, ArityAPI, ConstArity}
import org.shapesafe.core.axis.Axis
import org.shapesafe.core.axis.Axis.{->>, :<<-}
import org.shapesafe.core.tuple.{StaticTuples, Tuples}
import shapeless.{::, HList, HNil, Nat, Witness}

/**
  * a thin wrapper of HList that has all proofs of constraints included
  * this saves compiler burden and reduces error
  */
trait StaticShape extends LeafShape with StaticShape.Proto {

  type Record <: HList // name: String -> arity: Arity
  def record: Record

  type _Names <: Names
  val names: _Names

  type _Dimensions <: Dimensions.Tuple
  val dimensions: _Dimensions

  lazy val runtimeShape: List[Axis] = asList

//  final override def nodeString: String = this.toString
}

object StaticShape extends Tuples {

  final type VBound = Axis

  object Proto extends StaticTuples[VBound]
  type Proto = Proto.Tuple

  final type Tuple = StaticShape

  // Cartesian product doesn't have eye but whatever
  class Eye extends Proto.Eye with StaticShape {

    final type Record = HNil
    override def record: Record = HNil

    final override type _Names = Names.Eye
    final override val names = Names.Eye

    final override type _Dimensions = Dimensions.Eye
    final override val dimensions = Dimensions.Eye
  }
  override val Eye = new Eye

  // cartesian product symbol
  class ><[
      TAIL <: Tuple,
      HEAD <: VBound
  ](
      override val tail: TAIL,
      override val head: HEAD
  ) extends Proto.><[TAIL, HEAD](tail, head)
      with StaticShape {

    final type Field = head.Field
    final override type Record = Field :: tail.Record
    override lazy val record: Record = head.asField :: tail.record

    final override type _Names = Names.><[tail._Names, head.Name]
    final override val names = new Names.><(tail.names, head.nameW.value)

    final override type _Dimensions = Dimensions.><[tail._Dimensions, head._Arity]
    final override val dimensions = new Dimensions.><(tail.dimensions, head.arity)

    override type PeekHead = Head
  }

  override def cons[TAIL <: Tuple, HEAD <: VBound](tail: TAIL, head: HEAD): TAIL >< HEAD = new ><(tail, head)

  final type ><^[
      TAIL <: Tuple,
      HEAD <: Arity
  ] = ><[TAIL, ArityAPI.^[HEAD]]

  trait FromArities_Imp0 extends HListIntake {

    implicit def namelessInductive[
        H_TAIL <: HList,
        TAIL <: Tuple,
        C <: Arity
    ](
        implicit
        forTail: H_TAIL ==> TAIL
    ): (C :: H_TAIL) ==> (TAIL ><^ C) = {

      forAll[C :: H_TAIL].==> { v =>
        val prev = apply(v.tail)
        val vHead = v.head: C
        val head: ArityAPI.^[C] = vHead.^

        val result = prev.^ appendInner head
        result
      }
    }
  }

  object FromArities extends FromArities_Imp0 {

    // TODO: merge with namelessInductive?
    implicit def inductive[
        H_TAIL <: HList,
        TAIL <: Tuple,
        N <: XString, // CAUTION: cannot be reduced to w.T! Scala compiler is too dumb to figure it out
        C <: Arity
    ](
        implicit
        forTail: H_TAIL ==> TAIL,
        w: Witness.Aux[N]
    ): ((N ->> C) :: H_TAIL) ==> (TAIL >< (C :<<- N)) = {

      forAll[(N ->> C) :: H_TAIL].==> { v =>
        val prev = apply(v.tail)
        val vHead: C = v.head
        val head: C :<<- N = vHead.^ :<<- w

        val result = prev.^ appendInner head

        result
      }
    }
  }

  object FromXInts extends HListIntake {

    implicit def inductive[
        H_TAIL <: HList,
        TAIL <: Tuple,
        HEAD <: XInt
    ](
        implicit
        forTail: H_TAIL ==> TAIL,
        w: Witness.Aux[HEAD]
    ): (HEAD :: H_TAIL) ==> (TAIL ><^ ConstArity.Literal[HEAD]) = {

      forAll[HEAD :: H_TAIL].==> { v =>
        val prev = forTail(v.tail)
        val head = Arity(w) // Arity.Impl(ConstArity.Literal(w))

        prev.^ appendInner head
      }
    }
  }

  object FromNats extends HListIntake {

    implicit def inductive[
        H_TAIL <: HList,
        TAIL <: Tuple,
        HEAD <: Nat
    ](
        implicit
        forTail: H_TAIL ==> TAIL,
        asOp: NatAsOp[HEAD]
    ) = {

      forAll[HEAD :: H_TAIL].==> { v =>
        val prev = apply(v.tail)
        val head = Arity.FromNat(v.head)

        prev.^ appendInner head
      }
    }
  }

}
