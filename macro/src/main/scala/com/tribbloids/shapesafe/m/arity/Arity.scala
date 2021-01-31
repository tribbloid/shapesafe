package com.tribbloids.shapesafe.m.arity

import com.tribbloids.shapesafe.m.arity.Utils.{NatAsOp, Op}
import com.tribbloids.graph.commons.util.{IDMixin, WideTyped}
import shapeless.{Nat, Witness}
import singleton.ops.{+, ==, Require}

import scala.language.implicitConversions
import scala.util.Try

trait Arity extends ProvenExpression {

  override type Out >: this.type <: Arity
  override def out: this.type = this

  def number: Int // run-time

  final lazy val tryNumber = Try(number)
  final def numberOpt: Option[Int] = tryNumber.toOption

  override lazy val valueStr: String = tryNumber
    .map(_.toString)
    .recover {
      case ee: Exception =>
        ee.getMessage
    }
    .get
}

object Arity {

  import Witness._

  trait Static[S] extends Arity.Known with ProveArity.Invar[S] with IDMixin {

//    override type Out >: this.type <: Static[S]

    def singleton: S

    override def _id: S = singleton

    @transient case object internal {

      // for test only
      def _can_+(w: Lt[Int])(implicit proof: SS + w.T): Unit = {}

      def proveSameType[N2](implicit proof: SS =:= N2): Unit = {}

      def proveEqualType[N2](implicit proof: Require[SS == N2]): Unit = {}

      // TODO: should be named proofEqual, require should do everything in runtime?
      def requireEqual(w: Lt[Int])(implicit proof: Require[SS == w.T]): Unit = {

        proveEqualType[w.T]

        require(w.value == number)
      }
    }
  }

  object Static {}

  class Derived[S <: Op](override val singleton: S) extends Static[S] {
    override lazy val number: Int = singleton.value.asInstanceOf[Int]
  }

  object Derived {

    implicit def summon[S <: Op](implicit s: S): Derived[S] = new Derived[S](s)
  }

  // this makes it impossible to construct directly from Int type
  class Literal[S <: Int](val singleton: S) extends Static[S] {

    override def number: Int = singleton
  }

  object Literal {

    implicit def summon[S <: Int](implicit w: Witness.Aux[S]): Literal[S] = {
      new Literal[S](w.value)
    }

    def apply(w: Witness.Lt[Int]): Literal[w.T] = {

      Literal.summon[w.T](w) //TODO: IDEA inspection error
    }
  }

  object FromNat {

    def apply[N <: Nat](v: N)(implicit ev: NatAsOp[N]): Derived[NatAsOp[N]] = {

      Derived.summon[NatAsOp[N]](ev) //TODO: IDEA inspection error
    }
  }

  def apply(w: Witness.Lt[Int]): Literal[w.T] = {
    Literal.apply(w)
  }

  // TODO: should use a third-party library or selectDynamic to remove boilerplate
  lazy val _0 = WideTyped(Arity(0))

  lazy val _1 = WideTyped(Arity(1))

  lazy val _2 = WideTyped(Arity(2))

  lazy val _3 = WideTyped(Arity(3))

  case object Narrow { //TODO: experimental, should be merged into above

    lazy val _0 = Arity(0)

    lazy val _1 = Arity(1)

    lazy val _2 = Arity(2)

    lazy val _3 = Arity(3)
  }

  // can be derived from variables instead of literals

  trait Known extends Arity

  trait Unsafe extends Arity with ProveArity.Unsafe

  object Unsafe {

//    def apply[T](w: TwoFace.Int[T]): Arity TODO: impl later
  }

  case object Unknown extends Unsafe {

    override def number = throw new UnsupportedOperationException("[??]")
  }

  case class Dynamic(number: Int) extends Known with Unsafe {}

  // TODO: these doesn't work
  //  see https://stackoverflow.com/questions/62205940/when-calling-a-scala-function-with-compile-time-macro-how-to-failover-smoothly
  //  trait OfIntLike_Implicit0 { // of lower priority in resolving
  //
  //    implicit def unsafe(number: Int): OfInt_Unsafe = OfInt_Unsafe(number)
  //  }
  //
  //  object OfIntLike extends OfIntLike_Implicit0 {
  //
  //    implicit def safe(w: Lt[Int]): OfInt[w.T] = OfInt.safe(w)
  //  }
}
