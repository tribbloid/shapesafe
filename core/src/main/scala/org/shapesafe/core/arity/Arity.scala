package org.shapesafe.core.arity

import org.shapesafe.core.arity.Arity.HasInfo
import org.shapesafe.core.arity.ArityAPI.^
import org.shapesafe.core.arity.LeafArity.{Derived, Literal}
import org.shapesafe.core.arity.Utils.NatAsOp
import org.shapesafe.core.debugging.InfoCT
import org.shapesafe.m.GetInfoOf
import shapeless.{Nat, Witness}

import scala.language.implicitConversions
import scala.util.Try

trait Arity {

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

trait Arity_Imp0 {

  // TODO: useless, scala implicit priority is broken

  //  import ProveArity.Factory._
  //
  //  implicit def lastResort[
  //      A <: LeafArity
  //  ](
  //      implicit
  //      ev: GetInfoOf.Type.From[A]
  //  ): A =>> Arity.LastResortInfo[A] = ProveArity.forAll[A].=>> { v =>
  //    Arity.LastResortInfo(v)
  //  }

  case class LastResortInfo[A <: Arity](self: Arity)(
      implicit
      val ev: GetInfoOf.Type.From[A]
  ) extends HasInfo {

    override def runtimeArity: Int = self.runtimeArity

    override type _Info = ev.Out
  }
}

object Arity extends Arity_Imp0 {

  trait Verifiable extends Arity {}

  object Unprovable extends Arity {
    override def runtimeArity: Int = throw new UnsupportedOperationException(s"cannot verified an Unprovable")
  }

  trait HasInfo extends Arity with InfoCT

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
