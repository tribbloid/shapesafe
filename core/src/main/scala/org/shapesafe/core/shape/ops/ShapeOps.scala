package org.shapesafe.core.shape.ops

import org.shapesafe.core.arity.ops.ArityOps
import org.shapesafe.core.shape.binary.OuterProduct
import org.shapesafe.core.shape.unary.{|<<-, CheckDistinct, CheckEinSum, Reorder}
import org.shapesafe.core.shape.{Names, Shape}
import shapeless.{HList, SingletonProductArgs}
import shapeless.ops.hlist.Reverse

class ShapeOps[SELF <: Shape](val self: SELF) extends ShapeOps.VectorOps[SELF] with ShapeOps.MatrixOps[SELF] {

  /**
    * assign new names
    */
  object withNames {

    def apply[N <: Names](newNames: N): |<<-[SELF, N] = {

      new |<<-[SELF, N](self, newNames)
    }
  }

  lazy val |<<- : withNames.type = withNames

  // no need for Names constructor
  object named extends SingletonProductArgs {

    def applyProduct[H1 <: HList, H2 <: HList](
        v: H1
    )(
        implicit
        reverse: Reverse.Aux[H1, H2],
        lemma: Names.FromLiterals.Case[H2]
    ): |<<-[SELF, lemma.Out] = {

      val out = lemma.apply(reverse(v))

      withNames.apply(out)
    }
  }

  lazy val |<<-* : named.type = named

  def ><[THAT <: Shape](
      that: THAT
  ): OuterProduct[SELF, THAT] = {

    OuterProduct(self, that)
  }

  def outer[THAT <: Shape](
      that: THAT
  ): OuterProduct[SELF, THAT] = {

    ><(that)
  }

  def einSum: CheckEinSum[SELF] = CheckEinSum(self)

  def contract[N <: Names](names: N): Reorder[CheckEinSum[SELF], N] = {

    einSum.->(names)
  }

  def flattenWith[THAT <: Shape](
      infix: ArityOps.Infix,
      that: THAT
  ): infix.SquashByName[OuterProduct[SELF, THAT]] = {

    val outered: OuterProduct[SELF, THAT] = self.outer(that)
    infix.SquashByName.On(outered)
  }

  def flatten(
      infix: ArityOps.Infix
  ): infix.SquashByName[SELF] = {

    infix.SquashByName.On(self)
  }

  def transpose[N <: Names](names: N): Reorder[CheckDistinct[SELF], N] = {

    Reorder(CheckDistinct(self), names)
  }

}

object ShapeOps {

  trait Base[SELF <: Shape] {

    def self: SELF
  }

  val i = Names("i")
  val ij = Names("i", "j")
  val jk = Names("j", "k")
  val ik = Names("i", "k")

  trait VectorOps[SELF <: Shape] extends Base[SELF] {

    def dot[THAT <: Shape](that: THAT) = {
      val s1 = self |<<- i
      val s2 = that |<<- i

      s1.einSum(s2) ->
        Names("i")
    }
  }

  trait MatrixOps[SELF <: Shape] extends Base[SELF] {

    def :*[THAT <: Shape](that: THAT) = {
      val s1 = self |<<- ij
      val s2 = that |<<- jk

      s1.einSum(s2) -> ik

    }
  }
}
