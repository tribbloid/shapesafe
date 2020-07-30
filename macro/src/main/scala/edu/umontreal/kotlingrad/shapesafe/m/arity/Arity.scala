package edu.umontreal.kotlingrad.shapesafe.m.arity

import edu.umontreal.kotlingrad.shapesafe.m.arity.Utils.Op
import graph.commons.util.WideTyped
import shapeless.Witness
import singleton.ops.{+, ==, Require}

import scala.language.implicitConversions

trait Arity extends Proven {

  override type Out = this.type
  override def out: Out = this

  def numberOpt: Option[Int] // run-time

  override lazy val valueStr: String = numberOpt.map(_.toString).getOrElse(s"[??]")
}

object Arity {

  import Witness._

  trait Const[S] extends Arity with Proof.Invar[S] {

    def singleton: S

    def number: Int

    final override def numberOpt: Option[Int] = Some(number)

    @transient protected[shapesafe] object internal {

      def canPlus(w: Lt[Int])(implicit proof: SS + w.T): Unit = {}

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

  object Const {

//    case class Same[S](self: Const[S]) extends Proof.Invar[S] {
//      override type Out = Const[S]
//
//      override def out: Out = self
//    }
//
//    implicit def same[S]: Const[S] Implies Same[S] = v => Same(v)
  }

  class FromOp[S <: Op](val singleton: S) extends Const[S] {
    override lazy val number: Int = singleton.value.asInstanceOf[Int]
  }

  object FromOp {

    implicit def summon[S <: Op](implicit s: S): FromOp[S] = new FromOp[S](s)
  }

  // this makes it impossible to construct directly from Int type
  class FromLiteral[S <: Int](val singleton: S) extends Const[S] {

    override def number: Int = singleton
  }

  object FromLiteral {

    implicit def summon[S <: Int](implicit w: Witness.Aux[S]): FromLiteral[S] = {
      new FromLiteral[S](w.value)
    }
  }

  def apply(w: Witness.Lt[Int]): FromLiteral[w.T] = {

    FromLiteral.summon[w.T](w) //TODO: IDEA inspection error
  }

  // TODO: should use a third-party library or selectDynamic to remove boilerplate
  lazy val _0 = WideTyped(Arity(0))

  lazy val _1 = WideTyped(Arity(1))

  lazy val _2 = WideTyped(Arity(2))

  lazy val _3 = WideTyped(Arity(3))

  // can be derived from variables instead of literals

  trait Unsafe extends Arity with Proof.UnsafeLike

  object Unsafe {

//    def apply[T](w: TwoFace.Int[T]): Arity TODO: impl later
  }

  case object Unknown extends Unsafe {

    override def numberOpt: Option[Int] = None
  }

  case class Var(number: Int) extends Unsafe {

    override def numberOpt: Option[Int] = Some(number)
  }

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
