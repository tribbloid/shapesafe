package org.shapesafe.core.arity.ops

import org.shapesafe.graph.commons.util.HasOuter
import org.shapesafe.core.arity.binary.{Op2, Op2Like, Require2}
import org.shapesafe.core.arity.{Arity, ArityAPI}
import org.shapesafe.core.axis.OldNameUpdaterSystem
import org.shapesafe.core.debugging.Expressions
import org.shapesafe.core.shape.binary.DimensionWise
import org.shapesafe.core.shape.unary.ReduceByName
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

    object Updaters extends OldNameUpdaterSystem(op)

    trait _HasOuter extends HasOuter {

      final override def outer: Infix.this.type = Infix.this
    }

    object _AppendByName extends ReduceByName with _HasOuter {
      object oldNameUpdater extends Updaters.Appender

      type _Unary = Expressions.AppendByName[Op#Debug[Unit, Unit]#_AsOpStr]
    }
//    type AppendByName[S1 <: Shape] = AppendByName._On[S1]

    object _SquashByName extends ReduceByName with _HasOuter {
      object oldNameUpdater extends Updaters.Squasher

      type _Unary = Expressions.SquashByName[Op#Debug[Unit, Unit]#_AsOpStr]
    }
//    type SquashByName[S1 <: Shape] = SquashByName._On[S1]

    object _DimensionWise extends DimensionWise with _HasOuter {
      override val op: Infix.this.Op = Infix.this.op

      type _Binary = Expressions.DimensionWise[Op#Debug[Unit, Unit]#_AsOpStr]
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
