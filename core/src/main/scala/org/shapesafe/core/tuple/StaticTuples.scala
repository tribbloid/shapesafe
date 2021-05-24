package org.shapesafe.core.tuple

import com.tribbloids.graph.commons.util.{IDMixin, TextBlock}
import org.shapesafe.core.debugging.OpStrs.OpStr
import org.shapesafe.core.debugging.Expressions.Expr
import org.shapesafe.core.debugging.{CanPeek, Expressions}
import org.shapesafe.core.util.RecordView
import shapeless.{::, HList, HNil, Witness}
import singleton.ops.+

import scala.language.implicitConversions

trait StaticTuples[UB] extends TupleSystem with CanFromStatic {

  import StaticTuples._

  final type UpperBound = UB

  trait Tuple extends IDMixin with CanPeek { // TODO: rename to `Tuple`

    type Static <: HList
    def static: Static
    lazy val staticView: RecordView[Static] = RecordView(static)

    def asList: List[UB]

    override protected def _id: Any = asList

    final type Cons[HH <: UB] = StaticTuples.this.><[this.type, UB]
    type _ConsExpr[PEEK <: CanPeek]
  }

  class Eye extends Tuple {

    override type Static = HNil
    override def static: HNil = HNil

    override def asList: List[UB] = Nil

    override lazy val toString: _AsStr = EYE.value

    final override type _AsStr = EYE.T

    final override type _ConsExpr[PEEK <: CanPeek] = Expr[PEEK]
    final override type _AsExpr = EYE.T
  }
  override lazy val Eye = new Eye

  // cartesian product symbol
  class ><[
      TAIL <: Tuple,
      HEAD <: UB
  ](
      val tail: TAIL,
      val head: HEAD
  ) extends Tuple {

    // in scala 3 these will be gone
    type Tail = TAIL
    type Head = HEAD

    override type Static = HEAD :: tail.Static
    override def static: Static = head :: tail.static

    override def asList: List[UB] = tail.asList ++ Seq(head)

//    override lazy val toString = s"${tail.toString} >< $head"
    override lazy val toString: String = {
      s"""${tail.toString} ><
           |${TextBlock(head.toString).indent("  ").build}
           | """.stripMargin.trim
    }

    type PeekHead <: CanPeek

    final override type _AsStr = OpStr[TAIL] + " >< " + OpStr[PeekHead]

    final override type _ConsExpr[PEEK <: CanPeek] = Expressions.><[Expr[this.type], Expr[PEEK]]
//    final override type _Expr = Expr.><[Expr[TAIL], Expr[PeekHead]]
    final override type _AsExpr = TAIL#_ConsExpr[PeekHead]
  }
}

object StaticTuples {

  val EYE = Witness("➊")
//  implicit def toEyeOps(s: TupleSystem[_]): s.Impl.InfixOps[s.Eye] = new s.Impl.InfixOps(s.Eye)

  trait Total[UB] extends StaticTuples[UB] {

    implicit def consAlways[TAIL <: Tuple, HEAD <: UB]: Cons.FromFn2[TAIL, HEAD, TAIL >< HEAD] = {

      Cons.from[TAIL, HEAD].to { (tail, head) =>
        new ><(tail, head)
      }
    }
  }

  object W {

    final val eye = Witness("Eye")

    final val >< = Witness(" >< ")
  }
}
