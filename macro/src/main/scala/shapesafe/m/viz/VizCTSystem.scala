package shapesafe.m.viz

import ai.acyclic.prover.commons.reflect.Reflection
import ai.acyclic.prover.commons.reflect.format.TypeFormat
import ai.acyclic.prover.commons.viz.{TypeViz, TypeVizFormat}
import ai.acyclic.prover.commons.{HasOuter, TreeFormat}
import shapesafe.m.{Emit, MWithReflection}
import singleton.ops.+

import scala.collection.mutable
import scala.reflect.api.{Trees, Types}
import scala.reflect.macros.whitebox

trait VizCTSystem extends Product {

  def format: TypeVizFormat

  def useTree: Boolean

  final def typeFormat: TypeFormat = format.base
  final def treeFormat: TreeFormat = format.treeFormat

  trait Info[T] {
    type Out
  }
  object Info {

    type Aux[T, O <: String] = Info[T] { type Out = O }
    type Lt[T, O <: String] = Info[T] { type Out <: O }

    class Impl[T, O] extends Info[T] {
      final type Out = O
    }
  }
  def createInfoOf[T, O] = new Info.Impl[T, O]

  def apply[I]: Instance[I] = Instance[I]()

  def infer[T](v: T): Instance[T] = apply[T]

  def narrow[T](v: T): Instance[v.type] = apply[v.type]

  case class Instance[I]() {

    def summonInfo[O <: String with Singleton](
        implicit
        ev: Info.Aux[I, O]
    ): ev.type = ev

    def summonStr[O <: String with Singleton](
        implicit
        ev: Info.Aux[I, O],
        str: ("" + O) { type Out <: String }
    ): str.Out = str.value

    def peek[O <: String with Singleton](
        implicit
        ev: Info.Aux[I, O],
        emit: Emit.Info[O]
    ): Unit = {}

    def interrupt[O <: String with Singleton](
        implicit
        ev: Info.Aux[I, O],
        emit: Emit.Error[O]
    ): Unit = {}

    // TODO: impl Should_=:= at compile-time
  }

  lazy val runtime: TypeViz[Reflection.Runtime.type] = TypeViz.withFormat(this.format)

  trait SubSystem extends VizCTSystem with HasOuter {

    override def outer: VizCTSystem = VizCTSystem.this

    override def format: TypeVizFormat = VizCTSystem.this.format
    override def useTree: Boolean = VizCTSystem.this.useTree
  }
}

object VizCTSystem {

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

              val system = refl.typeView(tt).onlyInstance
              system.asInstanceOf[VizCTSystem]
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

            viz.withFormat(self.format).of(tt).treeString
          } else {

            refl.typeView(tt).formattedBy(self.typeFormat).text
          }

          str
        } catch {
          case e: Throwable =>
            println(s"[ERROR] Compile-time formatting exception: ${e.toString}")
            e.printStackTrace()

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
