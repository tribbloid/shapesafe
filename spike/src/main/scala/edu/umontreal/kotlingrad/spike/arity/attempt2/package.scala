package edu.umontreal.kotlingrad.spike.arity

import shapeless.Witness
import singleton.ops.+
import singleton.ops.impl.Op
import scala.language.implicitConversions

import scala.languageFeature.implicitConversions

package object attempt2 {

  // doesn't extend T => R intentionally
  trait Implies[-T, +R] {
    def transform(v: T): R
  }

  implicit def implies[T, R](v: T)(implicit bound: T Implies R): R = bound.transform(v)

  case class Summoner[R]() {
    def summon[T](v: T)(implicit ev: Implies[T, R]): R = ev.transform(v)
  }

  trait Operand {
    def +[
        X >: this.type <: Operand,
        Y <: Operand
    ](that: Y): Op2[X, Y] = {
      Op2[X, Y](this, that)
    }
  }
//  object Operand {
//    abstract class ProvenToBe[O <: Arity]()(implicit val out: O) extends Operand {}
//    object ProvenToBe {
//      implicit def trivial[O <: Arity, T <: ProvenToBe[O]]: MyTransform[T, Trivial[O, T]] = self => new Trivial(self)
//
//      /*implicit*/
//      class Trivial[O <: Arity, T <: ProvenToBe[O]](
//          val self: T
//      ) extends Proof {
//        override type Out = O
//        override def out: Out = self.out
//      }
//    }
//  }

  trait Proof extends Serializable {
    def self: Operand
    type Out <: Arity
    def out: Out
  }
  object Proof {
    trait Out_=[+O <: Arity] extends Proof {
      type Out <: O
    }

    trait Invar[S] extends Out_=[Arity.Const[S]] {
      type SS = S
    }
  }

  trait Arity extends Operand {}
  object Arity {
    trait Const[S] extends Arity {
      type SS = S
    }
    object Const {
      implicit def same[S]: Const[S] Implies Same[S] = self => new Same(self)

      /*implicit*/
      class Same[S](val self: Const[S]) extends Proof.Invar[S] {
        override type Out = Const[S]
        override def out: Const[S] = self
      }
    }

    class FromOp[S <: Op]() extends Const[S]
    object FromOp {
      implicit def summon[S <: Op](implicit s: S): FromOp[S] = new FromOp[S]()
    }

    class FromLiteral[S <: Int](val w: Witness.Lt[Int]) extends Const[S] {}
    object FromLiteral {
      implicit def summon[S <: Int](implicit w: Witness.Aux[S]): FromLiteral[S] =
        new FromLiteral[S](w)
    }

    def apply(w: Witness.Lt[Int]): FromLiteral[w.T] = {
      FromLiteral.summon[w.T](w) //TODO: IDEA inspection error
    }
  }

  case class Op2[
      +A1 <: Operand,
      +A2 <: Operand
  ](
      a1: A1,
      a2: A2
  ) extends Operand {}

  object Op2 {
    implicit def proveInvar[A1 <: Operand, A2 <: Operand, S1, S2](
        implicit
        bound1: A1 Implies Proof.Invar[S1],
        bound2: A2 Implies Proof.Invar[S2],
        lemma: S1 + S2,
    ): Op2[A1, A2] Implies ProveInvar[A1, A2, S1, S2] =
      self => new ProveInvar(self, lemma)

    /*implicit*/
    class ProveInvar[
        A1 <: Operand,
        A2 <: Operand,
        S1,
        S2
    ](
        val self: Op2[A1, A2],
        val lemma: S1 + S2
    ) extends Proof.Invar[S1 + S2] {
      override type Out = Arity.FromOp[S1 + S2]
      override def out: Out = Arity.FromOp.summon[S1 + S2](lemma)
    }
  }

  implicit val a = Arity(3)
  implicit val b = Arity(4)

  {

    val op = a + b

    op: Proof // compiles

    val summoner = Summoner[Proof]()
    summoner.summon(op)
  }

  {
    val op = a + b + b

    op: Proof // compiles

    val summoner = Summoner[Proof]()
    summoner.summon(op)
  }

  {
    val op = a + b + a + b

    op: Proof // compiles

    val summoner = Summoner[Proof]()
    summoner.summon(op)
  }
}
