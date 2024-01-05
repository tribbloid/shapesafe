package shapesafe.core.tuple

import shapeless.{::, HList}

import scala.language.implicitConversions

trait Tuples extends TupleSystem {

  type ><[TAIL <: Tuple, HEAD <: VBound] <: Tuple

  def cons[TAIL <: Tuple, HEAD <: VBound](tail: TAIL, head: HEAD): TAIL >< HEAD

  trait ConsIntake[B <: VBound] extends HListIntake {

    implicit def inductive[
        H_TAIL <: HList,
        TAIL <: Tuple,
        HEAD <: B
    ](
        implicit
        forTail: H_TAIL =>> TAIL
    ): (HEAD :: H_TAIL) =>> ><[TAIL, HEAD] = {

      forAll[HEAD :: H_TAIL].defining { v =>
        val prev = apply(v.tail)

        cons(prev, v.head)
      }
    }
  }

  object FromStatic extends ConsIntake[VBound] {}

  object FromLiterals extends ConsIntake[VBound with Singleton] {}

  trait InfixMixin[SELF <: Tuple] {

    def self: SELF

    def ><[
        HEAD <: VBound
    ](
        head: HEAD
    ): SELF >< HEAD = cons(self, head)

    def &[
        HEAD <: VBound
    ](
        head: HEAD
    ): SELF >< HEAD = cons(self, head)
  }

  implicit class tupleExtension[SELF <: Tuple](val self: SELF) extends InfixMixin[SELF] {}

  implicit def eyeExtension(s: this.type): tupleExtension[Eye] = tupleExtension[Eye](Eye)
}
