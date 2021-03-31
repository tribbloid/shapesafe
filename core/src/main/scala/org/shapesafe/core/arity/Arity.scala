package org.shapesafe.core.arity

import org.shapesafe.core.arity.ArityAPI.^
import org.shapesafe.core.arity.LeafArity.{Derived, Literal}
import org.shapesafe.core.arity.Utils.NatAsOp
import org.shapesafe.core.util.CompileMsgs
import shapeless.{Nat, Witness}

import scala.language.implicitConversions
import scala.util.Try

trait Arity extends CompileMsgs.ToString {

  def runtimeArity: Int
  final lazy val runtimeTry: Try[Int] = Try(runtimeArity)

  lazy val valueStr: String = runtimeTry
    .map(_.toString)
    .recover {
      case ee: Exception =>
        ee.getMessage
    }
    .get

  lazy val fullStr: String = {

    valueStr + ":" + this.getClass.getSimpleName
  }

  final override def toString: String = fullStr
}

object Arity {

  object Unprovable extends Arity {
    override def runtimeArity: Int = throw new UnsupportedOperationException(s"cannot verified an Unprovable")
  }

  implicit class Converters[A <: Arity](self: A) {

    def ^ : ^[A] = ArityAPI.^(self)
  }

  def apply(w: Witness.Lt[Int]): ^[Literal[w.T]] = {
    ^(Literal.apply(w))
  }

  lazy val _0 = Arity(0)

  lazy val _1 = Arity(1)

  lazy val _2 = Arity(2)

  lazy val _3 = Arity(3)

  object FromNat {

    def apply[N <: Nat](v: N)(
        implicit
        ev: NatAsOp[N]
    ): ^[Derived[NatAsOp[N]]] = {

      ^(Derived.summon[NatAsOp[N]](ev))
    }
  }
}
