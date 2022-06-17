package shapesafe.m

import ai.acyclic.graph.commons.reflect.Reflection
import singleton.ops.impl.Op

import java.util.logging.Logger
import scala.language.experimental.macros
import scala.reflect.macros.blackbox

/**
  * An alternative to RequireMsgSym in singleton-ops
  */
class GenericMsgEmitter[T, SS <: GenericMsgEmitter.EmitLevel] {}

object GenericMsgEmitter {

  trait EmitLevel

  trait Info extends EmitLevel
  trait Warning extends EmitLevel
  trait Error extends EmitLevel
  trait Abort extends EmitLevel

  def create[A, SS <: GenericMsgEmitter.EmitLevel]: GenericMsgEmitter[A, SS] = new GenericMsgEmitter[A, SS]

  def apply[SS <: GenericMsgEmitter.EmitLevel]: Level[SS] = Level[SS]()

  case class Level[SS <: GenericMsgEmitter.EmitLevel]() {

    def byOp[A <: Op](
        implicit
        _op: A
    ): GenericMsgEmitter[A, SS] =
      macro Macros.byOp[A, SS]

    def byType[A, SSS <: GenericMsgEmitter.EmitLevel](
        implicit
        _ttg: Reflection.Runtime.TypeTag[A]
    ): GenericMsgEmitter[A, SSS] =
      macro Macros.byTypeTag[A, SSS]

    def byOnlyInstance[A]: GenericMsgEmitter[A, SS] = macro Macros.bySingletonName[A, SS]
  }

  implicit def byOnlyInstance[A, SS <: GenericMsgEmitter.EmitLevel]: GenericMsgEmitter[A, SS] =
    macro Macros.bySingletonName[A, SS]

  final class Macros(val c: blackbox.Context) extends MWithReflection {

    import u._

    def outer: GenericMsgEmitter.type = GenericMsgEmitter.this

    def byOp[A: c.WeakTypeTag, LL: c.WeakTypeTag](_op: c.Tree): c.Tree = {

      val aa: Type = weakTypeOf[A]
      val tree = q"${_op}.value"
      val expr = c.Expr[Any](c.untypecheck(tree))

      val v: Any = c.eval(expr)

      val ll = emitValue[LL](v)

      q"$liftOuter.create[$aa, $ll]"
    }

    def emitValue[LL: c.WeakTypeTag](v: Any): Type = {
      val ss = "" + v

      val ll: Type = weakTypeOf[LL]

      // if inherited from multiple traits, take the most serious one
      if (ll <:< weakTypeOf[Abort]) {
        Logger.getLogger(this.getClass.getName).severe(ss)
//        println("throwing exception: " + ss)
//        throw new AbortMacroException(c.enclosingPosition.asInstanceOf[scala.reflect.internal.util.Position], ss)
        throw new Throwable(ss)

//        c.error(c.enclosingPosition, ss)
//        c.abort(c.enclosingPosition, ss)
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

      ll
    }

    def byTypeTag[A: c.WeakTypeTag, LL: c.WeakTypeTag](_ttg: c.Tree): c.Tree = {

      val aa: Type = weakTypeOf[A]

      val ttg: Reflection.Runtime.TypeTag[A] = c.eval(c.Expr[Reflection.Runtime.TypeTag[A]](c.untypecheck(q"${_ttg}")))
      val v = Reflection.Runtime.typeView(ttg.tpe).singletonName

      val ll = emitValue[LL](v)

      q"$liftOuter.create[$aa, $ll]"
    }

    def bySingletonName[A: c.WeakTypeTag, LL: c.WeakTypeTag]: c.Tree = {

      val aa: Type = weakTypeOf[A]
      val v = refl.typeView(aa).singletonName

      val ll = emitValue[LL](v)

      q"$liftOuter.create[$aa, $ll]"
    }
  }
}
