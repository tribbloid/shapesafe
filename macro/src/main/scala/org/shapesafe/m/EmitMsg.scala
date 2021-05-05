package org.shapesafe.m

import java.util.logging.Logger
import scala.language.experimental.macros
import scala.reflect.macros.whitebox

class EmitMsg[T, SS <: EmitMsg.EmitLevel] {

//  def emit: Unit = macro EmitMsg.Macros.emit[T, SS]
}

object EmitMsg {

  trait EmitLevel

  trait Info extends EmitLevel
  trait Warning extends EmitLevel
  trait Error extends EmitLevel
  trait Abort extends EmitLevel

  def create[A, SS <: EmitMsg.EmitLevel]: EmitMsg[A, SS] = new EmitMsg[A, SS]

  implicit def emit[A, SS <: EmitMsg.EmitLevel]: EmitMsg[A, SS] = macro Macros.emit[A, SS]

  final class Macros(val c: whitebox.Context) extends MWithReflection {

    import u._

    def outer: EmitMsg.type = EmitMsg.this

    def emit[A: c.WeakTypeTag, LL: c.WeakTypeTag]: c.Tree = {

      val aa: Type = weakTypeOf[A]
      val v = aa match {
        case v: u.ConstantType => v.value.value
        case _ =>
          throw new UnsupportedOperationException(
            s"type $aa is not a constant"
          )
      }
      val ss = "" + v
      val ll: Type = weakTypeOf[LL]

      // if inherited from multiple traits, take the most serious one
      if (ll <:< weakTypeOf[Abort]) {
        c.abort(c.enclosingPosition, ss)
      } else if (ll <:< typeOf[Error]) {
        Logger.getLogger(this.getClass.getName).severe(ss)
        c.error(c.enclosingPosition, ss)
      } else if (ll <:< typeOf[Warning]) {
        Logger.getLogger(this.getClass.getName).warning(ss)
        c.warning(c.enclosingPosition, ss)
      } else if (ll <:< typeOf[Info]) {
        Logger.getLogger(this.getClass.getName).info(ss)
        c.info(c.enclosingPosition, ss, force = true)
      } else {
        throw new UnsupportedOperationException(
          s"type $ll is not an EmitLevel"
        )
      }

      q"$liftOuter.create[$aa, $ll]"
    }
  }
}
