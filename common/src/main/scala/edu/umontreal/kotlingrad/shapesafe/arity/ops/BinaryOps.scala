package edu.umontreal.kotlingrad.shapesafe.arity.ops

import edu.umontreal.kotlingrad.shapesafe.arity.Arity
import edu.umontreal.kotlingrad.shapesafe.arity.ArityOp.BinaryOp
import edu.umontreal.kotlingrad.shapesafe.arity.ops.BinaryOps.Facet
import edu.umontreal.kotlingrad.shapesafe.arity.proof.MayEqual
import singleton.ops.{==, Require}

trait BinaryOps[-A1 <: Arity, -A2 <: Arity] {

  import singleton.ops._

  type FacetImpl[ConstOp[_, _]] <: Facet

  def getFacet[ConstOp[_, _]]: FacetImpl[ConstOp]

  abstract class Member[ConstOp[_, _]](
      val constOp: (Int, Int) => Int
  ) extends BinaryOp[A1, A2] {

    val facet: FacetImpl[ConstOp] = getFacet[ConstOp]

    type Out = facet.Out

    final override def out(a1: A1, a2: A2): Out = facet.out(constOp(a1.number, a2.number))
  }

  object Plus extends Member[+](_ + _)

  object Minus extends Member[-](_ - _)

  object Times extends Member[*](_ * _)

  object Divide extends Member[/](_ / _)
}

object BinaryOps {

  trait Facet {

    type Out <: Arity
    def out(v: Int): Out
  }

  case class Const[N1, N2]() extends BinaryOps[Arity.Constant[N1], Arity.Constant[N2]] {

    class FacetImpl[ConstOp[_, _]] extends Facet {

      type NOut = ConstOp[N1, N2]

      override type Out = Arity.FromInt[NOut]
      override def out(v: Int): Arity.FromInt[NOut] = Arity.FromInt.apply(v)
    }

    override def getFacet[ConstOp[_, _]]: FacetImpl[ConstOp] = new FacetImpl[ConstOp]

    type A1 = Arity.Constant[N1]
    type A2 = Arity.Constant[N2]

    case class Equal()(implicit lemma: Require[N1 == N2]) extends MayEqual[A1, A2] {

      override type Out = A1

      override def _out(a1: A1, a2: A2): A1 = a1
    }
  }

  implicit def const[N1, N2]: Const[N1, N2] = Const()

  implicit def unsafe[A1 <: Arity, A2 <: Arity](implicit lemma: A1 ?? A2): ??[A1, A2] = lemma
}
