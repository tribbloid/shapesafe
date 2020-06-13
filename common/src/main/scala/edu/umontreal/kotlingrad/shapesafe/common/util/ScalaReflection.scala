package edu.umontreal.kotlingrad.shapesafe.common.util

trait ScalaReflection extends Serializable {

  /** The universe we work in (runtime or macro) */
  val universe: scala.reflect.api.Universe

  /** The mirror used to access types in the universe */
  def mirror: universe.Mirror

}

object ScalaReflection extends ScalaReflection {

  val universe: scala.reflect.runtime.universe.type = scala.reflect.runtime.universe

  // Since we are creating a runtime mirror using the class loader of current thread,
  // we need to use def at here. So, every time we call mirror, it is using the
  // class loader of the current thread.
  override def mirror: universe.Mirror = {
    universe.runtimeMirror(Thread.currentThread().getContextClassLoader)
  }
}
