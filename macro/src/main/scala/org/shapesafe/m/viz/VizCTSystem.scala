package org.shapesafe.m.viz

import com.tribbloids.graph.commons.util.reflect.Reflection
import com.tribbloids.graph.commons.util.reflect.format.TypeFormat
import com.tribbloids.graph.commons.util.viz.{TypeViz, TypeVizFormat}
import com.tribbloids.graph.commons.util.{HasOuter, TreeFormat}
import org.shapesafe.m.{EmitMsg, MWithReflection}
import shapeless.Witness
import singleton.ops.{+, RequireMsg, RequireMsgSym}

import scala.collection.mutable
import scala.reflect.api.{Trees, Types}
import scala.reflect.macros.whitebox

trait VizCTSystem extends Product {

  import VizCTSystem._

  def format: TypeVizFormat

  def useTree: Boolean

  final def typeFormat: TypeFormat = format.base
  final def treeFormat: TreeFormat = format.treeFormat

  trait InfoOf[T] {
    type Out
  }
  object InfoOf {

    type Aux[T, O <: String] = InfoOf[T] { type Out = O }
    type Lt[T, O <: String] = InfoOf[T] { type Out <: O }

    class Impl[T, O] extends InfoOf[T] {
      final type Out = O
    }
  }
  def createInfoOf[T, O] = new InfoOf.Impl[T, O]

  def apply[I]: Instance[I] = Instance[I]()

  def infer[T](v: T): Instance[T] = apply[T]

  def narrow[T](v: T): Instance[v.type] = apply[v.type]

  case class Instance[I]() {

    def summon[O <: String](
        implicit
        ev: InfoOf.Aux[I, O]
    ): ev.type = ev

    def peek[O <: String](
        implicit
        ev: InfoOf.Aux[I, O],
        emit: EmitInfo["\n" + O]
    ): Unit = {}

    def interrupt[O <: String](
        implicit
        ev: InfoOf.Aux[I, O],
        emit: EmitError["\n" + O]
    ): Unit = {}

    // TODO: impl Should_=:= at compile-time
  }

  lazy val runtime: TypeViz[Reflection.Runtime.type] = TypeViz.formattedBy(this.format)

  trait Updated extends VizCTSystem with HasOuter {

    override def outer: VizCTSystem = VizCTSystem.this

    override def format: TypeVizFormat = VizCTSystem.this.format
    override def useTree: Boolean = VizCTSystem.this.useTree
  }
}

object VizCTSystem {

  val FALSE = Witness(false)

  type EmitError[T] = RequireMsg[FALSE.T, T]

  type EmitWarning[T] = RequireMsgSym[FALSE.T, T, singleton.ops.Warn]
  //  type EmitInfo[T] = EmitWarning[T] // should change after the patch

  //  type EmitWarning[T] = EmitMsg[T, EmitMsg.Warning]
  type EmitInfo[T] = EmitMsg[T, EmitMsg.Info]

  private val cache = mutable.HashMap.empty[Types#Type, (VizCTSystem, Trees#Tree)]

  trait MBase extends MWithReflection {

    def outer: VizCTSystem.type = VizCTSystem.this

    override val c: whitebox.Context

    import u._

    def getSystem(tt: Type): (VizCTSystem, c.Tree) = {

      cache
        .getOrElseUpdate(
          tt, {
            val self: VizCTSystem = {

              val r = refl.TypeView(tt).getOnlyInstance
              r.asInstanceOf[VizCTSystem]
            }
            val name: String = self.getClass.getCanonicalName.stripSuffix("$")
            val liftSelf: c.Tree = c.parse(name)
            self -> liftSelf
          }
        )
        .asInstanceOf[(VizCTSystem, c.Tree)]
    }

    def infoOf[
        T: c.WeakTypeTag,
        SELF <: VizCTSystem: c.WeakTypeTag
    ]: c.Tree = {

      val tt: Type = weakTypeOf[T]

      val (self, liftSelf) = getSystem(weakTypeOf[SELF].dealias)

      val result =
        try {

          val useTree = self.useTree

          val str = if (useTree) {

            viz.formattedBy(self.format).of(tt).treeString
          } else {

            refl.TypeView(tt).formattedBy(self.typeFormat).text
          }

          str
        } catch {
          case e: Throwable =>
            "[ ERROR ] " + e.toString
        }

      q"$liftSelf.createInfoOf[$tt, $result]"
    }

    //    def T[A: WeakTypeTag]: Tree = {
    //
    //      val tt: Type = weakTypeOf[A]
    //      val str = refl.TypeView(tt).Display(format).base
    //
    //      val w = Witness(str)
    //      val sTT = w.T
    // TODO: figure out how it works in shapeless?

    //      new SingletonTypeMacros(c).fieldTypeCarrier(sTT)
    //    }

    //    def report[A: WeakTypeTag](fn: String => Unit): Tree = {
    //
    //      val aa: Type = weakTypeOf[A]
    //      val str = viz.of(aa).typeTree.treeString
    //
    //      fn(str)
    ////      c.abort(c.enclosingPosition, str)
    //
    //      q"Unit"
    //    }

  }

  final class Macros(val c: whitebox.Context) extends MBase
}
