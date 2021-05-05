package org.shapesafe.m.issue

import scala.language.experimental.macros
import scala.reflect.macros.whitebox

// TODO: this feature is blocked by: https://github.com/scala/bug/issues/12388
class EmitMsg[T] {}

object EmitMsg {

  implicit def emit[A]: EmitMsg[A] = macro Macros.emit[A]

  final class Macros(val c: whitebox.Context) {

    def emit[A: c.WeakTypeTag]: c.Tree = {
      c.abort(c.enclosingPosition, "ERROR!")
    }
  }
}
