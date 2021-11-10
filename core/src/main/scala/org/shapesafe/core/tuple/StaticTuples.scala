package org.shapesafe.core.tuple

import org.shapesafe.core.debugging.{CanPeek, Expressions}
import org.shapesafe.core.util.HListView
import org.shapesafe.graph.commons.util.{IDMixin, TextBlock}
import shapeless.{::, HList, HNil, Witness}

trait StaticTuples[VB] extends Tuples {

  import StaticTuples._

  final type VBound = VB

  trait Tuple extends IDMixin with CanPeek {

    type Static <: HList
    def static: Static
    lazy val staticView: HListView[Static] = HListView(static)

    def asList: List[VB]

    override protected def _id: Any = asList

    final type Cons[HH <: VB] = StaticTuples.this.><[this.type, VB]
    type _ConsExpr[PEEK <: CanPeek]
  }

  class Eye extends Tuple {

    override type Static = HNil
    override def static: HNil = HNil

    override def asList: List[VB] = Nil

    override lazy val toString: String = EYE.value

    final override type _ConsExpr[T <: CanPeek] = T#Expr
    final override type Expr = EYE.T
  }
  override val Eye = new Eye

  // cartesian product symbol
  class ><[
      TAIL <: Tuple,
      HEAD <: VB
  ](
      val tail: TAIL,
      val head: HEAD
  ) extends Tuple {

    // in scala 3 these will be gone
    type Tail = TAIL
    type Head = HEAD

    override type Static = HEAD :: tail.Static
    override def static: Static = head :: tail.static

    override def asList: List[VB] = tail.asList ++ Seq(head)

    override lazy val toString: String = {
      val tailStr = tail match {
        case _: Eye => ""
        case _ => tail.toString + " ><\n"
      }

      s"""$tailStr${TextBlock(head.toString).indent("  ").build}
         | """.stripMargin.trim
    }

    type PeekHead <: CanPeek

    final override type _ConsExpr[PEEK <: CanPeek] = Expressions.><[this.Expr, PEEK#Expr]
    final override type Expr = TAIL#_ConsExpr[PeekHead]
  }

  final override def cons[TAIL <: Tuple, HEAD <: VBound](tail: TAIL, head: HEAD): TAIL >< HEAD =
    new ><(tail, head)
}

object StaticTuples {

  val EYE = Witness("∅")

  object W {

    final val eye = Witness("Eye")

    final val >< = Witness(" >< ")
  }
}
