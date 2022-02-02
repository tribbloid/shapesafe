package shapesafe.core.arity.ops

import ai.acyclic.graph.commons.HasOuter
import shapesafe.core.Const
import shapesafe.core.arity.binary.{Op2, Op2Like, Require2}
import shapesafe.core.arity.{Arity, ArityType}
import shapesafe.core.axis.OldNameUpdaters
import shapesafe.core.debugging.{DebugConst, Expressions}
import shapesafe.core.shape.binary.Op2ByDim
import shapesafe.core.shape.unary.AccumulateByName
import shapesafe.core.shape.{LeafShape, Shape}
import singleton.ops

trait ArityOpsLike extends HasArity {
  // this allows all subclasses of Op2 to be defined once

  import ArityOpsLike._

  trait Infix {
    type Op <: Op2Like
    def op: Op

    type On[A1 <: ArityType, A2 <: ArityType] = Op#On[A1, A2]

    final type apply[A1 <: ArityType, A2 <: ArityType] = On[A1, A2]
    def apply(that: Arity): Arity.^[On[_ArityType, that._ArityType]] = op.on(arityType.^, that).^

    object Updaters extends OldNameUpdaters(op)

    trait _HasOuter extends HasOuter {

      final override def outer: Infix.this.type = Infix.this
    }

    object _AppendByName extends AccumulateByName with _HasOuter {

      val oldNameUpdater: Updaters.Appender.type = Updaters.Appender

      type _Unary = Expressions.AppendByName[Op#Debug[Unit, Unit]#_DebugSymbol]
    }

    object _ReduceByName extends AccumulateByName with _HasOuter {

      val oldNameUpdater: Updaters.Reducer.type = Updaters.Reducer

      type _Unary = Expressions.ReduceByName[Op#Debug[Unit, Unit]#_DebugSymbol]
    }

    object _Op2ByDim_Strict extends Op2ByDim with _HasOuter {

      override val op: Infix.this.Op = Infix.this.op

      override type _ExprProto = Expressions.Op2ByDim_Strict[Op#Debug[Unit, Unit]#_DebugSymbol]

      override type Iff[A <: LeafShape, B <: LeafShape] = A#NatNumOfDimensions =:= B#NatNumOfDimensions

      override type _RefuteProto = "Dimension mismatch"
    }

    object _Op2ByDim_DropLeft extends Op2ByDim with _HasOuter {
      override val op: Infix.this.Op = Infix.this.op

      override type _ExprProto = Expressions.Op2ByDim_DropLeft[Op#Debug[Unit, Unit]#_DebugSymbol]

      override type Iff[_ <: LeafShape, _ <: LeafShape] = Const.True

      override type _RefuteProto = DebugConst.INTERNAL_ERROR.type
    }

    // part of the API

    // TODO: should it be "foldByName"? Underlying Op may not be monoidal
    def reduceByName[S1 <: Shape](s1: S1) = {
      _ReduceByName.On(s1.shapeType).^
    }
    def reduceByName[S1 <: Shape, S2 <: Shape](s1: S1, s2: S2) = {
      _ReduceByName.On((s1 >< s2).shapeType).^
    }

    def applyByDim[S1 <: Shape, S2 <: Shape](s1: S1, s2: S2) = {
      _Op2ByDim_Strict.On[s1._ShapeType, s2._ShapeType](s1.shapeType, s2.shapeType).^
    }

    def applyByDimDropLeft[S1 <: Shape, S2 <: Shape](s1: S1, s2: S2) = {
      _Op2ByDim_DropLeft.On[S1#_ShapeType, S2#_ShapeType](s1.shapeType, s2.shapeType).^
    }
  }

  class InfixImpl[OP <: Op2Like](val op: OP) extends Infix {

    type Op = OP
  }

//  object :+ extends Op2[ops.+] with Infix
  object :+ extends InfixImpl(Op2[ops.+, Expressions.+])
  type :+[X <: ArityType, Y <: ArityType] = :+.On[X, Y]

  object :- extends InfixImpl(Op2[ops.-, Expressions.-])
  type :-[X <: ArityType, Y <: ArityType] = :-.On[X, Y]

  object :* extends InfixImpl(Op2[ops.*, Expressions.*])
  type :*[X <: ArityType, Y <: ArityType] = :*.On[X, Y]

  object :/ extends InfixImpl(Op2[ops./, Expressions./])
  type :/[X <: ArityType, Y <: ArityType] = :/.On[X, Y]

  object ==! extends InfixImpl(RequireEqual)
  type ==![X <: ArityType, Y <: ArityType] = ==!.On[X, Y]

  object !=! extends InfixImpl(Require2[ops.!=, Expressions.!=])
  type !=![X <: ArityType, Y <: ArityType] = !=!.On[X, Y]

  object `<!` extends InfixImpl(Require2[ops.<, Expressions.<])
  type `<!`[X <: ArityType, Y <: ArityType] = `<!`.On[X, Y]

  object >! extends InfixImpl(Require2[ops.>, Expressions.>])
  type >![X <: ArityType, Y <: ArityType] = >!.On[X, Y]

  object <=! extends InfixImpl(Require2[ops.<=, Expressions.<=])
  type <=![X <: ArityType, Y <: ArityType] = <=!.On[X, Y]

  object >=! extends InfixImpl(Require2[ops.>=, Expressions.>=])
  type >=![X <: ArityType, Y <: ArityType] = >=!.On[X, Y]
}

object ArityOpsLike {

  val RequireEqual: Require2.Impl[ops.==, Expressions.==] = Require2[ops.==, Expressions.==]
}
