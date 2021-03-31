package org.shapesafe.m

import com.tribbloids.graph.commons.util.reflect.format.TypeFormat

import scala.language.experimental.macros
import scala.reflect.macros.whitebox

object TypeToLiteral {

  final val rootPath: String = this.getClass.getCanonicalName.stripSuffix("$")

  trait Base extends Product {

    def format: TypeFormat

    trait Case[T] {
      type Out
    }
    type Aux[T, O <: String] = Case[T] { type Out = O }
    type Lt[T, O <: String] = Case[T] { type Out <: O }

    class %%[T, O] extends Case[T] {
      final type Out = O
    }
    def %%[T, O] = new %%[T, O]

    case class From[I]() {

      def summon[O <: String](
          implicit
          ev: Aux[I, O]
      ): ev.type = ev
    }

    trait MacroBase extends MWithReflection {

      override val c: whitebox.Context

      import u._

      val liftBase: c.Tree = {

        val name: String = Base.this.getClass.getCanonicalName.stripSuffix("$")
        c.parse(name)
      }

      def apply[A: c.WeakTypeTag]: Tree = {

        val tt: Type = weakTypeOf[A]
        val str = refl.TypeView(tt).formattedBy(format).fullText
        q"$liftBase.%%[$tt, $str]"
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

  case object Type extends Base {

    override def format: TypeFormat = TypeFormat.Type.Short

    final class Macros(val c: whitebox.Context) extends MacroBase
    implicit def apply[I]: Case[I] = macro Macros.apply[I]
  }

  case object Kind extends Base {

    override def format: TypeFormat = TypeFormat.Kind.Short

    final class Macros(val c: whitebox.Context) extends MacroBase
    implicit def apply[I]: Case[I] = macro Macros.apply[I]
  }
}
