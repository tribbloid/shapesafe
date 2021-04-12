package org.shapesafe.m

import com.tribbloids.graph.commons.util.diff.StringDiff

import scala.language.experimental.macros
import scala.reflect.macros.whitebox

object TypeVizCT {

  class TypeOfCT[A] {

    def debug: Unit = macro Macros.show[A]

    def shouldBe[B]: Unit = macro Macros.shouldBe[A, B]
  }

  final class Macros(val c: whitebox.Context) extends MWithReflection {

    import c.universe._

    def show[A: WeakTypeTag]: Tree = {

      val aa: Type = weakTypeOf[A]

      val str = viz.fromType(aa).typeTree.treeString

      throw new DebugInfo("\n" + str)
    }

    def shouldBe[A: WeakTypeTag, B: WeakTypeTag]: Tree = {

      val aa: Type = weakTypeOf[A]
      val bb: Type = weakTypeOf[B]

      if (!(aa =:= bb)) {
        val Seq(s1, s2) = Seq(aa, bb).map { v =>
          Option(viz.fromType(v).typeTree.treeString)
        }

        val diff = StringDiff(s1, s2, Seq(this.getClass))

        throw new AssertionError(diff.errorStr)
      }

      q"Unit"
    }
  }

  def apply[T] = new TypeOfCT[T]

  def infer[T](v: T): TypeOfCT[T] = apply[T]

  def narrow[T](v: T): TypeOfCT[v.type] = apply[v.type]
}
