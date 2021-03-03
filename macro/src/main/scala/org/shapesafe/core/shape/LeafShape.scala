package org.shapesafe.core.shape

import org.shapesafe.core.shape.ProveShape.=>>
import org.shapesafe.core.arity.Utils.NatAsOp
import org.shapesafe.core.arity.{Arity, LeafArity}
import org.shapesafe.core.axis.Axis
import org.shapesafe.core.axis.Axis.{->>, :<<-}
import org.shapesafe.core.shape.ops.LeafShapeOps
import org.shapesafe.core.tuple.{CanFromStatic, StaticTuples, TupleSystem}
import org.shapesafe.core.util.RecordView
import shapeless.ops.hlist.At
import shapeless.ops.nat.ToInt
import shapeless.ops.record.Selector
import shapeless.{::, HList, HNil, Nat, Poly1, Witness}

import scala.language.implicitConversions

/**
  * a thin wrapper of HList that has all proofs of constraints included
  * this saves compiler burden and reduces error
  */
trait LeafShape extends Shape with LeafShape.Proto {

  type Record <: HList // name: String -> dim: arity.Expression
  def record: Record
  lazy val recordView: RecordView[Record] = RecordView(record)

  lazy val getField: recordView.GetField.type = recordView.GetField

  type _Names <: Names
  val names: _Names

  type _Dimensions <: Dimensions.Impl
  val dimensions: _Dimensions

  lazy val runtimeShape: List[Axis] = asList

  object IndexLookup extends Poly1 {

    implicit def name[S <: String](
        implicit
        _selector: Selector[Record, S] { type Out <: Arity }
    ): Case[Index.Name[S]] {
      type Result = _selector.Out :<<- S
    } = at[Index.Name[S]] { name =>
      val arity = _selector(record)
      arity :<<- name.w
    }

    implicit def ii[N <: Nat](
        implicit
        _at: At[Static, N] { type Out <: Axis }
    ): Case[Index.I_th[N]] {
      type Result = _at.Out
    } = at[Index.I_th[N]] { index =>
      _at(static)
    }
  }
  type IndexLookup = IndexLookup.type

  object Sub {

    def apply[T <: Index](v: T): Sub1[T] = {

      Sub1(v)
    }

    def apply(i: Nat)(
        implicit
        toIntN: ToInt[i.N]
    ): Sub1[Index.I_th[i.N]] = {

      apply(Index.I_th(i))
    }

    def apply(w: Witness.Lt[String]): Sub1[Index.Name[w.T]] = {

      apply(Index.Name(w))
    }
  }

  case class Sub1[T <: Index](index: T) {

    def axis(
        implicit
        byIndex: IndexLookup.Case[T]
    ): byIndex.Result = {
      byIndex.apply(index)
    }
  }

  final override def nodeString: String = this.toString
}

object LeafShape extends TupleSystem with CanFromStatic {

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

    final override type _Dimensions = Dimensions.><[tail._Dimensions, head.Dimension]
    final override val dimensions = new Dimensions.><(tail.dimensions, head.dimension)
  }

  trait FromRecord_Imp0 extends AbstractFromHList {

    implicit def namelessInductive[
        H_TAIL <: HList,
        TAIL <: Impl,
        D <: Arity
    ](
        implicit
        forTail: H_TAIL ==> TAIL
    ): (D :: H_TAIL) ==> (TAIL >< D) = {

      forAll[D :: H_TAIL].==> { v =>
        val prev = apply(v.tail)
        val vHead = v.head: D

        prev >|< vHead
      }
    }
  }

  object FromRecord extends FromRecord_Imp0 {

    implicit def inductive[
        H_TAIL <: HList,
        TAIL <: Impl,
        N <: String, // CAUTION: cannot be reduced to w.T! Scala compiler is too dumb to figure it out
        D <: Arity
    ](
        implicit
        forTail: H_TAIL ==> TAIL,
        w: Witness.Aux[N]
    ): ((N ->> D) :: H_TAIL) ==> (TAIL >< (D :<<- N)) = {

      forAll[(N ->> D) :: H_TAIL].==> { v =>
        val prev = apply(v.tail)
        val vHead = v.head: D
        val head: D :<<- N = vHead :<<- w

        prev >|< head
      }
    }

  }

  implicit def consAlways[TAIL <: Impl, HEAD <: UpperBound]: Cons.FromFn2[TAIL, HEAD, TAIL >< HEAD] = {

    Cons.from[TAIL, HEAD].to { (tail, head) =>
      new ><(tail, head)
    }
  }

  implicit def toOps[T <: LeafShape](self: T): LeafShapeOps[T] = new LeafShapeOps(self)

  //TODO: doesn't work, blocked by https://github.com/milessabin/shapeless/issues/1072
  object FromLiterals extends AbstractFromHList {

    implicit def inductive[
        H_TAIL <: HList,
        TAIL <: Impl,
        HEAD <: Int
    ](
        implicit
        forTail: H_TAIL ==> TAIL,
        w: Witness.Aux[HEAD]
    ): (HEAD :: H_TAIL) ==> ><[TAIL, LeafArity.Literal[w.T]] = {

      forAll[w.T :: H_TAIL].==> { v =>
        val prev = apply(v.tail)
        val head = LeafArity.Literal(w)

        prev >|< head
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
        ev: NatAsOp[HEAD]
    ): (HEAD :: H_TAIL) ==> ><[TAIL, LeafArity.Derived[NatAsOp[HEAD]] :<<- Axis.unknownName.type] = {

      forAll[(HEAD :: H_TAIL)].==> { v =>
        val prev = apply(v.tail)
        val head = LeafArity.FromNat(v.head) :<<- Axis.unknownName

        prev >|< head
      }
    }
  }

  implicit def endo[T <: LeafShape]: T =>> T = ProveShape.forAll[T].=>>(identity[T])
}
