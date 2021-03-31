package org.shapesafe.m

import com.tribbloids.graph.commons.util.debug.print_@
import shapeless.{HList, Witness}

import scala.language.experimental.macros
import scala.reflect.macros.whitebox

object Type2String {

  def apply[I]: Witness.Lt[String] = macro Macros.apply[I]

  final class Macros(val c: whitebox.Context) extends MWithReflection {

    import c.universe._

    def apply[A: WeakTypeTag]: Tree = {

      val tt: Type = weakTypeOf[A]
      val str = tt.toString

      val vv = viz(tt)
//      print_@(vv.toString)

      q"shapeless.Witness.mkWitness[$str]($str)"
    }
  }
}
