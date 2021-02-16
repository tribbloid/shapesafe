package org.shapesafe.core.shape

import org.shapesafe.core.arity.Utils.NatAsOp
import org.shapesafe.core.arity.{Arity, LeafArity}
import org.shapesafe.core.axis.Axis
import org.shapesafe.core.axis.Axis.{->>, :<<-}
import org.shapesafe.core.shape.binary.{EinSumIndexed, EinSumOps}
import org.shapesafe.core.tuple.{CanFromStatic, StaticTuples, TupleSystem}
import org.shapesafe.core.util.RecordView
import shapeless.ops.hlist.{At, Reverse, ZipWithKeys}
import shapeless.ops.nat.ToInt
import shapeless.ops.record.Selector
import shapeless.{::, HList, HNil, Nat, NatProductArgs, Poly1, Witness}

import scala.language.implicitConversions

/**
  * a thin wrapper of HList that has all proofs of constraints included
  * this saves compiler burden and reduces error
  */
trait Shape extends Shape.Proto {

  type Record <: HList // name: String -> dim: arity.Expression
  def record: Record
  lazy val recordView: RecordView[Record] = RecordView(record)

  lazy val getField: recordView.GetField.type = recordView.GetField

  type _Names <: Names
  val names: _Names

  type _Dimensions <: Dimensions.Impl
  val dimensions: _Dimensions

  /**
    * assign new names
    * @param newNames a tuple of names
    */
  def |<<-[
      ZZ <: HList,
      O <: Shape
  ](newNames: Names)(
      implicit
      zipping: ZipWithKeys.Aux[newNames.Keys, dimensions.Static, ZZ],
      prove: Shape.FromRecord.==>[ZZ, O]
  ): O = {

    val zipped: ZZ = dimensions.static.zipWithKeys(newNames.keys)
    Shape.FromRecord(zipped)
  }

  object IndexToAxis extends Poly1 {

    implicit def name[S <: String](
        implicit
        _selector: Selector[Record, S] { type Out <: Arity }
    ): Case[FieldIndex.Named[S]] {
      type Result = _selector.Out :<<- S
    } = at[FieldIndex.Named[S]] { name =>
      val arity = _selector(record)
      arity :<<- name.w
    }

    implicit def ii[N <: Nat](
        implicit
        _at: At[Static, N] { type Out <: Axis }
    ): Case[FieldIndex.N_th[N]] {
      type Result = _at.Out
    } = at[FieldIndex.N_th[N]] { index =>
      _at(static)
    }
  }

  object Sub {

    def apply[T <: FieldIndex](v: T): Sub1[T] = {

      Sub1(v)
    }

    def apply(i: Nat)(
        implicit
        toIntN: ToInt[i.N]
    ): Sub1[FieldIndex.N_th[i.N]] = {

      apply(FieldIndex.N_th(i))
    }

    def apply(w: Witness.Lt[String]): Sub1[FieldIndex.Named[w.T]] = {

      apply(FieldIndex.Named(w))
    }
  }

  case class Sub1[T <: FieldIndex](index: T) {

    def axis(
        implicit
        byIndex: IndexToAxis.Case[T]
    ): byIndex.Result = {
      byIndex.apply(index)
    }
  }
}

object Shape extends TupleSystem with CanFromStatic with NatProductArgs {

  final type UpperBound = Axis

  object Proto extends StaticTuples[UpperBound]
  type Proto = Proto.Impl

  final type Impl = Shape

  // Cartesian product doesn't have eye but whatever
  object eye extends Proto.EyeLike with Shape {

    final type Record = HNil
    override def record: Record = HNil

    final override type _Names = Names.Eye
    final override val names = Names.Eye

    final override type _Dimensions = Dimensions.Eye
    final override val dimensions = Dimensions.Eye
  }

  // cartesian product symbol
  class ><[
      TAIL <: Impl,
      HEAD <: UpperBound
  ](
      override val tail: TAIL,
      override val head: HEAD
  ) extends Proto.><[TAIL, HEAD](tail, head)
      with Shape {

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
    ): (D :: H_TAIL) ==> (TAIL >< (D :<<- Axis.emptyName.type)) = {

      from[D :: H_TAIL].to { v =>
        val prev = apply(v.tail)
        val vHead = v.head: D
        val head = vHead :<<- Axis.emptyName

        prev >< head
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

      from[(N ->> D) :: H_TAIL].to { v =>
        val prev = apply(v.tail)
        val vHead = v.head: D
        val head: D :<<- N = vHead :<<- w

        prev >< head
      }
    }

  }

  implicit def consAlways[TAIL <: Impl, HEAD <: UpperBound]: Cons.FromFn[TAIL, HEAD, TAIL >< HEAD] = {

    Cons[TAIL, HEAD].to { (tail, head) =>
      new ><(tail, head)
    }
  }

  implicit def ops[SELF <: Shape](self: SELF): ShapeOps[SELF] = {

    new ShapeOps(self)
  }

  implicit def toEyeOps(v: this.type): ShapeOps[Eye] = ops[Eye](Eye)

  implicit def einSumOps[
      S <: Shape
  ](self: S)(
      implicit
      checkThis: EinSumIndexed.FromStatic.Case[self.Record]
  ) = {

    val indexed = EinSumIndexed.FromStatic.apply(self.record)

    EinSumOps(Seq(self))(indexed)
  }

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
    ): (HEAD :: H_TAIL) ==> ><[TAIL, LeafArity.Literal[w.T] :<<- Axis.emptyName.type] = {

      from[w.T :: H_TAIL].to { v =>
        val prev = apply(v.tail)
        val head = LeafArity.Literal(w) :<<- Axis.emptyName

        prev >< head
      }
    }

  }

//  def applyProduct[T <: HList](v: T)(implicit ev: FromLiterals.Case[T]): ev.Out = {
//    ev.apply(v)
//  }

  object FromNats extends AbstractFromHList {

    implicit def inductive[
        H_TAIL <: HList,
        TAIL <: Impl,
        HEAD <: Nat
    ](
        implicit
        forTail: H_TAIL ==> TAIL,
        ev: NatAsOp[HEAD]
    ): (HEAD :: H_TAIL) ==> ><[TAIL, LeafArity.Derived[NatAsOp[HEAD]] :<<- Axis.emptyName.type] = {

      from[(HEAD :: H_TAIL)].to { v =>
        val prev = apply(v.tail)
        val head = LeafArity.FromNat(v.head) :<<- Axis.emptyName

        prev >< head
      }
    }

  }

  // TODO: should the reverse be justified?
  def applyNatProduct[H1 <: HList, H2 <: HList](
      v: H1
  )(
      implicit
      reverse: Reverse.Aux[H1, H2],
      ev: FromNats.Case[H2]
  ): ev.Out = {
    FromNats.apply(v.reverse)
  }

}
