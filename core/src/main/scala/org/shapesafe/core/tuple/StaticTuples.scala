package org.shapesafe.core.tuple

import com.tribbloids.graph.commons.util.{IDMixin, TextBlock}
import org.shapesafe.core.util.{CompileMsgs, RecordView}
import shapeless.{::, HList, HNil, Witness}

import scala.language.implicitConversions

trait StaticTuples[UB] extends TupleSystem with CanFromStatic {

  final type UpperBound = UB

  trait Impl extends IDMixin { // TODO: rename to `Tuple`

    type Static <: HList
    def static: Static
    lazy val staticView: RecordView[Static] = RecordView(static)

    def asList: List[UB]

    override protected def _id: Any = asList

    type ><[HEAD <: UB] = StaticTuples.this.><[this.type, UB]
  }

  class Eye extends Impl {

    override type Static = HNil
    override def static: HNil = HNil

    override def asList: List[UB] = Nil

    override lazy val toString = "Eye"
  }
  override lazy val Eye = new Eye

  // cartesian product symbol
  class ><[
      TAIL <: Impl,
      HEAD <: UB
  ](
      val tail: TAIL,
      val head: HEAD
  ) extends Impl {

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
  }
}

object StaticTuples {

//  implicit def toEyeOps(s: TupleSystem[_]): s.Impl.InfixOps[s.Eye] = new s.Impl.InfixOps(s.Eye)

  trait Total[UB] extends StaticTuples[UB] {

    implicit def consAlways[TAIL <: Impl, HEAD <: UB]: Cons.FromFn2[TAIL, HEAD, TAIL >< HEAD] = {

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
