package com.tribbloids.shapesafe.m.arity

import com.tribbloids.shapesafe.m.arity.Utils.Op
import com.tribbloids.graph.commons.util.WideTyped
import shapeless.Witness
import singleton.ops.{+, ==, Require}

import scala.language.implicitConversions
import scala.util.Try

trait Arity extends Proven {

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

  trait Static[S] extends Arity.Known with OfArity.Invar[S] {

//    override type Out >: this.type <: Static[S]

    def singleton: S

    @transient case object internal {

      // for test only
      def _can_+(w: Lt[Int])(implicit proof: SS + w.T): Unit = {}

      def proveSame[N2](implicit proof: SS =:= N2): Unit = {}

      // 'prove' means happening only in compile-time
      def proveEqual[N2](implicit proof: Require[SS == N2]): Unit = {

        //TODO: need run-time proof?
      }

      // TODO: should be named proofEqual, require should do everything in runtime
      def requireEqual(w: Lt[Int])(implicit proof: Require[SS == w.T]): Unit = {

        proveEqual[w.T]

        require(w.value == number)
      }
    }
  }

  object Static {}

  class FromOp[S <: Op](val singleton: S) extends Static[S] {
    override lazy val number: Int = singleton.value.asInstanceOf[Int]
  }

  object FromOp {

    implicit def summon[S <: Op](implicit s: S): FromOp[S] = new FromOp[S](s)
  }

  // this makes it impossible to construct directly from Int type
  class FromLiteral[S <: Int](val singleton: S) extends Static[S] {

    override def number: Int = singleton
  }

  object FromLiteral {

    implicit def summon[S <: Int](implicit w: Witness.Aux[S]): FromLiteral[S] = {
      new FromLiteral[S](w.value)
    }

    def apply(w: Witness.Lt[Int]): FromLiteral[w.T] = {

      FromLiteral.summon[w.T](w) //TODO: IDEA inspection error
    }
  }

  def apply(w: Witness.Lt[Int]): FromLiteral[w.T] = {
    FromLiteral.apply(w)
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

  trait Unsafe extends Arity with OfArity.Unsafe

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
