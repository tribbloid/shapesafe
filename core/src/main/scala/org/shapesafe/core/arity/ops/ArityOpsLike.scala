package org.shapesafe.core.arity.ops

import com.tribbloids.graph.commons.util.HasOuter
import org.shapesafe.core.arity.Arity
import org.shapesafe.core.arity.binary.{AssertEqual, Op2, Op2Like}
import org.shapesafe.core.axis.OldNameUpdaterSystem
import org.shapesafe.core.shape.Shape
import org.shapesafe.core.shape.unary.Op2ByName
import singleton.ops

trait ArityOpsLike[A <: Arity] {
  // this allows all subclasses of Op2 to be defined once

  def self: A

  trait Infix {
    type Op <: Op2Like
    def op: Op

    type On[A1 <: Arity, A2 <: Arity] = Op#On[A1, A2]

    def apply[Y <: Arity](that: Y): On[A, Y] = op.on(self, that)

    object Updaters extends OldNameUpdaterSystem(op)

    object AppendByName extends Op2ByName with HasOuter {
      object oldNameUpdater extends Updaters.Appender

      override def outer: AnyRef = Infix.this
    }
    type AppendByName[S1 <: Shape] = AppendByName._On[S1]

    object SquashByName extends Op2ByName with HasOuter {
      object oldNameUpdater extends Updaters.Squasher

      override def outer: AnyRef = Infix.this
    }
    type SquashByName[S1 <: Shape] = SquashByName._On[S1]
  }

  class InfixImpl[OP <: Op2Like](val op: OP) extends Infix {

    type Op = OP
  }

//  object :+ extends Op2[ops.+] with Infix
  object :+ extends InfixImpl(new Op2[ops.+])
  type :+[X <: Arity, Y <: Arity] = :+.On[X, Y]

  object :- extends InfixImpl(new Op2[ops.-])
  type :-[X <: Arity, Y <: Arity] = :-.On[X, Y]

  object :* extends InfixImpl(new Op2[ops.*])
  type :*[X <: Arity, Y <: Arity] = :*.On[X, Y]

  object :/ extends InfixImpl(new Op2[ops./])
  type :/[X <: Arity, Y <: Arity] = :/.On[X, Y]

  object :=!= extends InfixImpl(AssertEqual)
  type :=!=[X <: Arity, Y <: Arity] = :=!=.On[X, Y]
}
