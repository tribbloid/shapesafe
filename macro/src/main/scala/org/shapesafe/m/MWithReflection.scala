package org.shapesafe.m

import org.shapesafe.graph.commons.util.reflect.MacroReflection
import org.shapesafe.graph.commons.util.viz.TypeViz

import scala.reflect.macros.blackbox

trait MWithReflection {

  def outer: AnyRef

  val c: blackbox.Context
  val u: c.universe.type = c.universe

  lazy val liftOuter: c.Tree = {

    val name: String = outer.getClass.getCanonicalName.stripSuffix("$")
    c.parse(name)
  }

  lazy val refl = MacroReflection[u.type](c.universe)
  lazy val viz = TypeViz(refl)

//  case class MacroError(message: String) extends Exception(message)
}
