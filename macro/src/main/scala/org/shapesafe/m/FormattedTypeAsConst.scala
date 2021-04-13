package org.shapesafe.m

import com.tribbloids.graph.commons.util.reflect.format.{TypeFormat, TypeInfoOvrd}

import scala.language.experimental.macros
import scala.reflect.macros.whitebox

trait FormattedTypeAsConst extends Product {

  def format: TypeFormat

  trait Case[T] {
    type Out <: String
    def out: Out
  }

  type Aux[T, O <: String] = Case[T] { type Out = O }
  type Lt[T, O <: String] = Case[T] { type Out <: O }

  class %%[T, O <: String](val out: O) extends Case[T] {
    final type Out = O
  }
  def %%[T, O <: String](out: O) = new %%[T, O](out)

  case class From[I]() {

    def summon[O <: String](
        implicit
        ev: Aux[I, O]
    ): ev.type = ev
  }

  trait MacroBase extends ReflectionM {

    override val c: whitebox.Context

    import u._

    val liftBase: c.Tree = {

      val name: String = FormattedTypeAsConst.this.getClass.getCanonicalName.stripSuffix("$")
      c.parse(name)
    }

    def apply[A: c.WeakTypeTag]: Tree = {

      val tt: Type = weakTypeOf[A]
      val str = refl.TypeView(tt).formattedBy(format).fullText

      q"$liftBase.%%[$tt, $str]($str)"
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
  }
}

object FormattedTypeAsConst {

  final val rootPath: String = this.getClass.getCanonicalName.stripSuffix("$")

  case object Type extends FormattedTypeAsConst {

    override def format: TypeFormat = TypeFormat.Type +> TypeFormat.DeAlias +> TypeFormat.HidePackages

    final class Macros(val c: whitebox.Context) extends MacroBase
    implicit def apply[I]: Case[I] = macro Macros.apply[I]
  }

  case object Kind extends FormattedTypeAsConst {

    override def format: TypeFormat = TypeFormat.Kind +> TypeFormat.DeAlias +> TypeFormat.HidePackages

    final class Macros(val c: whitebox.Context) extends MacroBase
    implicit def apply[I]: Case[I] = macro Macros.apply[I]
  }

  case object Class extends FormattedTypeAsConst {

    override def format: TypeFormat = TypeFormat.Class +> TypeFormat.DeAlias +> TypeFormat.HidePackages

    final class Macros(val c: whitebox.Context) extends MacroBase
    implicit def apply[I]: Case[I] = macro Macros.apply[I]
  }

  case object Ovrd extends FormattedTypeAsConst {

    override def format: TypeFormat = TypeInfoOvrd(Type.format)

    final class Macros(val c: whitebox.Context) extends MacroBase
    implicit def apply[I]: Case[I] = macro Macros.apply[I]
  }
}
