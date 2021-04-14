package org.shapesafe.core.shape

import org.shapesafe.core.arity.Utils.NatAsOp
import org.shapesafe.core.arity.{Arity, ArityAPI, LeafArity}
import org.shapesafe.core.axis.Axis
import org.shapesafe.core.axis.Axis.{->>, :<<-}
import org.shapesafe.core.debugging.InfoCT.Peek
import org.shapesafe.core.tuple.{CanFromStatic, StaticTuples, TupleSystem}
import shapeless.{::, HList, HNil, Nat, Witness}
import singleton.ops.+

import scala.language.implicitConversions

/**
  * a thin wrapper of HList that has all proofs of constraints included
  * this saves compiler burden and reduces error
  */
trait LeafShape extends Shape with LeafShape.Proto {

  type Record <: HList // name: String -> arity: Arity
  def record: Record

  type _Names <: Names
  val names: _Names

  type _Dimensions <: Dimensions.Impl
  val dimensions: _Dimensions

  lazy val runtimeShape: List[Axis] = asList

  // TODO: merge with GetSubscript & becoming a special case
//  object IndexLookup extends Poly1 {
//
//    implicit def name[S <: String](
//        implicit
//        _selector: Selector[Record, S] { type Out <: Arity }
//    ) = at[Index.Name[S]] { name =>
//      val core = _selector(record)
//      core.^ :<<- name.w
//    }
//
//    implicit def ii[N <: Nat](
//        implicit
//        _at: At[Static, N] { type Out <: Axis }
//    ): Case[Index.I_th[N]] {
//      type Result = _at.Out
//    } = at[Index.I_th[N]] { index =>
//      _at(static)
//    }
//  }
//  type IndexLookup = IndexLookup.type
//
//  object Sub {
//
//    def apply[T <: Index](v: T): Sub1[T] = {
//
//      Sub1(v)
//    }
//
//    def apply(i: Nat)(
//        implicit
//        toIntN: ToInt[i.N]
//    ): Sub1[Index.I_th[i.N]] = {
//
//      apply(Index.I_th(i))
//    }
//
//    def apply(w: Witness.Lt[String]): Sub1[Index.Name[w.T]] = {
//
//      apply(Index.Name(w))
//    }
//  }
//
//  case class Sub1[T <: Index](index: T) {
//
//    def axis(
//        implicit
//        byIndex: IndexLookup.Case[T]
//    ): byIndex.Result = {
//      byIndex.apply(index)
//    }
//  }

  final override def nodeString: String = this.toString
}

object LeafShape extends TupleSystem with CanFromStatic {

  import org.shapesafe.core.shape.ProveShape.Factory._

  final type UpperBound = Axis

  object Proto extends StaticTuples[UpperBound]
  type Proto = Proto.Impl

  final type Impl = LeafShape

  // Cartesian product doesn't have eye but whatever
  class Eye extends Proto.Eye with LeafShape {

    final type Record = HNil
    override def record: Record = HNil

    final override type _Names = Names.Eye
    final override val names = Names.Eye

    final override type _Dimensions = Dimensions.Eye
    final override val dimensions = Dimensions.Eye
  }
  override lazy val Eye = new Eye

  // cartesian product symbol
  class ><[
      TAIL <: Impl,
      HEAD <: UpperBound
  ](
      override val tail: TAIL,
      override val head: HEAD
  ) extends Proto.><[TAIL, HEAD](tail, head)
      with LeafShape {

    final type Field = head.Field
    final override type Record = Field :: tail.Record
    override lazy val record: Record = head.asField :: tail.record

    final override type _Names = Names.><[tail._Names, head.Name]
    final override val names = tail.names >< head.nameSingleton

    final override type _Dimensions = Dimensions.><[tail._Dimensions, head._Arity]
    final override val dimensions = new Dimensions.><(tail.dimensions, head.arity)

    override type _PeekHead = Peek[Head]
  }

  final type ><^[
      TAIL <: Impl,
      HEAD <: Arity
  ] = ><[TAIL, ArityAPI.^[HEAD]]

  trait FromArity extends AbstractFromHList {

    implicit def namelessInductive[
        H_TAIL <: HList,
        TAIL <: Impl,
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

  object FromRecord extends FromArity {

    implicit def inductive[
        H_TAIL <: HList,
        TAIL <: Impl,
        N <: String, // CAUTION: cannot be reduced to w.T! Scala compiler is too dumb to figure it out
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

  implicit def consAlways[TAIL <: Impl, HEAD <: UpperBound]: Cons.FromFn2[TAIL, HEAD, TAIL >< HEAD] = {

    Cons.from[TAIL, HEAD].to { (tail, head) =>
      new ><(tail, head)
    }
  }

  object FromLiterals extends AbstractFromHList {

    implicit def inductive[
        H_TAIL <: HList,
        TAIL <: Impl,
        HEAD <: Int with Singleton
    ](
        implicit
        forTail: H_TAIL ==> TAIL,
        w: Witness.Aux[HEAD]
    ): (HEAD :: H_TAIL) ==> (TAIL ><^ LeafArity.Literal[HEAD]) = {

      forAll[HEAD :: H_TAIL].==> { v =>
        val prev = forTail(v.tail)
        val head = Arity(w) //  Arity.Impl(LeafArity.Literal(w))

        prev.^ appendInner head
      }
    }
  }

  object FromNats extends AbstractFromHList {

    implicit def inductive[
        H_TAIL <: HList,
        TAIL <: Impl,
        HEAD <: Nat
    ](
        implicit
        forTail: H_TAIL ==> TAIL,
        asOp: NatAsOp[HEAD]
    ): (HEAD :: H_TAIL) ==> (TAIL ><^ LeafArity.Derived[NatAsOp[HEAD]]) = {

      forAll[HEAD :: H_TAIL].==> { v =>
        val prev = apply(v.tail)
        val head = Arity.FromNat(v.head)

        prev.^ appendInner head
      }
    }
  }

  implicit def endo[T <: LeafShape]: T =>> T = ProveShape.forAll[T].=>>(identity[T])
}
