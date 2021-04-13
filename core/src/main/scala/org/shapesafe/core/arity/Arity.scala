package org.shapesafe.core.arity

import com.tribbloids.graph.commons.util.reflect.format.HasTypeInfo
import org.shapesafe.core.arity.ArityAPI.^
import org.shapesafe.core.arity.LeafArity.{Derived, Literal}
import org.shapesafe.core.arity.Utils.NatAsOp
import shapeless.{Nat, Witness}

import scala.language.implicitConversions
import scala.util.Try

trait Arity extends HasTypeInfo {

  def runtimeArity: Int
  final lazy val runtimeTry: Try[Int] = Try(runtimeArity)

  lazy val valueStr: String = runtimeTry
    .map(_.toString)
    .getOrElse("???")

  lazy val fullStr: String = {

    valueStr + ":" + this.getClass.getSimpleName
  }

  final override def toString: String = fullStr
}

trait Arity_Imp0 {}

object Arity extends Arity_Imp0 {

  trait Verifiable extends Arity {}

  object _Unprovable extends Arity {
    override def runtimeArity: Int = throw new UnsupportedOperationException(
      this.getClass.getSimpleName.stripSuffix("$")
    )
  }
  lazy val Unprovable: ^[_Unprovable.type] = _Unprovable.^

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
    ) = {

      ^(Derived.summon[NatAsOp[N]](ev))
    }
  }
}
