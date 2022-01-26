package shapesafe.core.arity.ops

import ai.acyclic.graph.commons.HasOuter
import shapesafe.core.arity.binary.{Op2, Op2Like, Require2}
import shapesafe.core.arity.{Arity, ArityAPI}
import shapesafe.core.axis.OldNameUpdaters
import shapesafe.core.debugging.Expressions
import shapesafe.core.shape.ShapeAPI
import shapesafe.core.shape.binary.Op2PerDim
import shapesafe.core.shape.unary.AccumulateByName
import singleton.ops

trait ArityOpsLike extends HasArity {
  // this allows all subclasses of Op2 to be defined once

  import ArityOpsLike._

  trait Infix {
    type Op <: Op2Like
    def op: Op

    type On[A1 <: Arity, A2 <: Arity] = Op#On[A1, A2]

    final type apply[A1 <: Arity, A2 <: Arity] = On[A1, A2]
    def apply(that: ArityAPI): ArityAPI.^[On[_Arity, that._Arity]] = op.on(arity.^, that).^

    object Updaters extends OldNameUpdaters(op)

    trait _HasOuter extends HasOuter {

      final override def outer: Infix.this.type = Infix.this
    }

    object _AppendByName extends AccumulateByName with _HasOuter {

      val oldNameUpdater: Updaters.Appender.type = Updaters.Appender

      type _Unary = Expressions.AppendByName[Op#Debug[Unit, Unit]#_DebugSymbol]
    }

    object _ReduceByName extends AccumulateByName with _HasOuter {

      val oldNameUpdater: Updaters.Squasher.type = Updaters.Squasher

      type _Unary = Expressions.ReduceByName[Op#Debug[Unit, Unit]#_DebugSymbol]
    }

    object _Op2PerDim extends Op2PerDim with _HasOuter {
      override val op: Infix.this.Op = Infix.this.op

      type _Binary = Expressions.Op2PerDim[Op#Debug[Unit, Unit]#_DebugSymbol]
    }

    // part of the API

    def reduceByName[S1 <: ShapeAPI](s1: S1) = {
      _ReduceByName.On(s1.shape).^
    }
    def reduceByName[S1 <: ShapeAPI, S2 <: ShapeAPI](s1: S1, s2: S2) = {
      _ReduceByName.On((s1 >< s2).shape).^
    }

    def applyPerDim[S1 <: ShapeAPI, S2 <: ShapeAPI](s1: S1, s2: S2) = {
      _Op2PerDim.On(s1.shape, s2.shape).^
    }
  }

  class InfixImpl[OP <: Op2Like](val op: OP) extends Infix {

    type Op = OP

  }

//  object :+ extends Op2[ops.+] with Infix
  object :+ extends InfixImpl(Op2[ops.+, Expressions.+])
  type :+[X <: Arity, Y <: Arity] = :+.On[X, Y]

  object :- extends InfixImpl(Op2[ops.-, Expressions.-])
  type :-[X <: Arity, Y <: Arity] = :-.On[X, Y]

  object :* extends InfixImpl(Op2[ops.*, Expressions.*])
  type :*[X <: Arity, Y <: Arity] = :*.On[X, Y]

  object :/ extends InfixImpl(Op2[ops./, Expressions./])
  type :/[X <: Arity, Y <: Arity] = :/.On[X, Y]

  object ==! extends InfixImpl(RequireEqual)
  type ==![X <: Arity, Y <: Arity] = ==!.On[X, Y]

  object !=! extends InfixImpl(Require2[ops.!=, Expressions.!=])
  type !=![X <: Arity, Y <: Arity] = !=!.On[X, Y]

  object `<!` extends InfixImpl(Require2[ops.<, Expressions.<])
  type `<!`[X <: Arity, Y <: Arity] = `<!`.On[X, Y]

  object >! extends InfixImpl(Require2[ops.>, Expressions.>])
  type >![X <: Arity, Y <: Arity] = >!.On[X, Y]

  object <=! extends InfixImpl(Require2[ops.<=, Expressions.<=])
  type <=![X <: Arity, Y <: Arity] = <=!.On[X, Y]

  object >=! extends InfixImpl(Require2[ops.>=, Expressions.>=])
  type >=![X <: Arity, Y <: Arity] = >=!.On[X, Y]
}

object ArityOpsLike {

  val RequireEqual: Require2.Impl[ops.==, Expressions.==] = Require2[ops.==, Expressions.==]
}
