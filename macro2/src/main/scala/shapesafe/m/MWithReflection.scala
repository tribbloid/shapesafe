package shapesafe.m

import ai.acyclic.prover.commons.refl.Reflection
import ai.acyclic.prover.commons.viz.TypeViz

import scala.reflect.macros.blackbox

trait MWithReflection {

  def outer: AnyRef

  val c: blackbox.Context
  val u: c.universe.type = c.universe

  lazy val liftOuter: c.Tree = {

    val name: String = outer.getClass.getCanonicalName.stripSuffix("$")
    c.parse(name)
  }

  object refl extends Reflection.CompileTime {

    final override lazy val universe: MWithReflection.this.u.type = MWithReflection.this.u

  }

  lazy val viz = TypeViz.default(refl)

//  case class MacroError(message: String) extends Exception(message)
}
