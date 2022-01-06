package shapesafe.core.arity

import shapesafe.core.arity
import shapesafe.core.arity.ArityAPI.^
import shapesafe.core.arity.ConstArity.{Derived, Literal}
import shapesafe.core.arity.Utils.NatAsOp
import shapesafe.core.debugging.{CanPeek, HasDebugSymbol}
import shapeless.{Nat, Witness}

import scala.util.Try

trait Arity extends CanPeek with HasDebugSymbol {

  def runtimeValue: Int
  final lazy val runtimeValueTry: Try[Int] = Try(runtimeValue)

  lazy val valueStr: String = runtimeValueTry
    .map(_.toString)
    .getOrElse("???")
//    .recover {
//      case ee: Exception =>
//        ee.getMessage
//    }
//    .get

  lazy val fullStr: String = {

    valueStr + ":" + this.getClass.getSimpleName
  }

  final override def toString: String = fullStr
}

object Arity {

  trait Verifiable extends Arity {}

  val Unprovable: ^[arity.Unprovable.type] = arity.Unprovable.^

  val Unchecked: ^[arity.Unchecked.type] = arity.Unchecked.^

  implicit class converters[A <: Arity](self: A) {

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
        asOp: NatAsOp[N]
    ): ^[Derived[NatAsOp[N], asOp.OutInt]] = {

      ^(Derived.summon[NatAsOp[N]](asOp))
    }
  }
}
