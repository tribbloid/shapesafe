package org.shapesafe.m

import com.tribbloids.graph.commons.util.diff.StringDiff

import scala.language.experimental.macros
import scala.reflect.macros.whitebox

// TODO: change to CompileTimeViz
object Should_=:= {

  lazy val objectClassPath: String = {
    val fullName = this.getClass.getCanonicalName.stripSuffix("$")
    s"_root_.$fullName"
  }

  def apply[A, B]: Unit = macro Macros.apply[A, B]

  def apply[A]: Unit = macro Macros.apply[A, Nothing]

  final class Macros(val c: whitebox.Context) extends MWithReflection {

    import c.universe._

    def apply[A: WeakTypeTag, B: WeakTypeTag]: Tree = {

      val aa: Type = weakTypeOf[A]
      val bb: Type = weakTypeOf[B]

      if (!(aa =:= bb)) {
        val Seq(s1, s2) = Seq(aa, bb).map { v =>
          Option(viz.apply(v).typeTree.treeString)
        }

        val diff = StringDiff(s1, s2, Seq(this.getClass))

        throw MacroError(
          diff.errorStr
        )
      }

      q"Unit" // TODO: make the object Liftable
    }
  }
}
