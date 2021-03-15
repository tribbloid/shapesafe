package org.shapesafe.m

import com.tribbloids.graph.commons.util.reflect.MacroReflection
import com.tribbloids.graph.commons.util.viz.TypeViz

import scala.reflect.macros.blackbox

trait MWithReflection {

  val c: blackbox.Context

  val refl = MacroReflection[c.universe.type](c.universe)
  val viz = TypeViz(refl)

  case class MacroError(message: String) extends Exception(message)
}
