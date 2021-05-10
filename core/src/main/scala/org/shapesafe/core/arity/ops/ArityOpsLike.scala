package org.shapesafe.core.arity.ops

import com.tribbloids.graph.commons.util.HasOuter
import org.shapesafe.core.arity.binary.{AssertEqual, Op2, Op2Like}
import org.shapesafe.core.arity.{Arity, ArityAPI}
import org.shapesafe.core.axis.OldNameUpdaterSystem
import org.shapesafe.core.debugging.Expr
import org.shapesafe.core.shape.binary.DimensionWise
import org.shapesafe.core.shape.unary.ReduceByName
import singleton.ops

trait ArityOpsLike extends HasArity {
  // this allows all subclasses of Op2 to be defined once

  trait Infix {
    type Op <: Op2Like
    def op: Op

    type On[A1 <: Arity, A2 <: Arity] = Op#On[A1, A2]

    def apply(that: ArityAPI): ArityAPI.^[On[_Arity, that._Arity]] = op.on(arity.^, that).^

    object Updaters extends OldNameUpdaterSystem(op)

    trait _HasOuter extends HasOuter {

      final override def outer: Infix.this.type = Infix.this
    }

    object _AppendByName extends ReduceByName with _HasOuter {
      object oldNameUpdater extends Updaters.Appender

      type _Unary = Expr.AppendByName[Op#Symbol[_, _]]
    }
//    type AppendByName[S1 <: Shape] = AppendByName._On[S1]

    object _SquashByName extends ReduceByName with _HasOuter {
      object oldNameUpdater extends Updaters.Squasher

      type _Unary = Expr.SquashByName[Op#Symbol[_, _]]
    }
//    type SquashByName[S1 <: Shape] = SquashByName._On[S1]

    object _DimensionWise extends DimensionWise with _HasOuter {
      override val op: Infix.this.Op = Infix.this.op

      type _Binary = Expr.DimensionWise[Op#Symbol[_, _]]
    }
  }

  class InfixImpl[OP <: Op2Like](val op: OP) extends Infix {

    type Op = OP
  }

//  object :+ extends Op2[ops.+] with Infix
  object :+ extends InfixImpl(new Op2[ops.+, Expr.Elementary.+])
  type :+[X <: Arity, Y <: Arity] = :+.On[X, Y]

  object :- extends InfixImpl(new Op2[ops.-, Expr.Elementary.-])
  type :-[X <: Arity, Y <: Arity] = :-.On[X, Y]

  object :* extends InfixImpl(new Op2[ops.*, Expr.Elementary.*])
  type :*[X <: Arity, Y <: Arity] = :*.On[X, Y]

  object :/ extends InfixImpl(new Op2[ops./, Expr.Elementary./])
  type :/[X <: Arity, Y <: Arity] = :/.On[X, Y]

  object :==! extends InfixImpl(AssertEqual)
  type :==![X <: Arity, Y <: Arity] = :==!.On[X, Y]
}
