package org.shapesafe.m

import com.tribbloids.graph.commons.util.reflect.TypeFormat

import scala.language.experimental.macros
import scala.reflect.macros.whitebox

object GetInfoOf {

  final val rootPath: String = this.getClass.getCanonicalName.stripSuffix("$")

  trait Base extends Product {

    def format: TypeFormat

    trait From[T] {
      type Out
    }

    class %%[T, O] extends From[T] {
      final type Out = O
    }
    def %%[T, O] = new %%[T, O]

    trait MacroBase extends MWithReflection {

      override val c: whitebox.Context

      import u._

      val liftBase: c.Tree = {

        val name: String = Base.this.getClass.getCanonicalName.stripSuffix("$")
        c.parse(name)
      }

      def apply[A: c.WeakTypeTag]: Tree = {

        val tt: Type = weakTypeOf[A]
        val str = refl.TypeView(tt).Display(format).full
//        val path = typeOf[Base.this.type].baseClasses.head.fullName

//        val base = {
//          val nn = u.TermName(this.getClass.getCanonicalName)
//          u.term
//        }

//        print_@(format)
//        print_@(tt.dealias.toString + " " + tt.getClass.getSimpleName)

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

    override def format: TypeFormat = TypeFormat(
      hidePackages = true,
      variants = TypeFormat.variants.DeAlias
    )

    final class Macros(val c: whitebox.Context) extends MacroBase
    implicit def apply[I]: From[I] = macro Macros.apply[I]
  }

  case object TypeConstructor extends Base {

    override def format: TypeFormat = TypeFormat(
      hidePackages = true,
      nameOf = TypeFormat.nameOf.TypeConstructor,
      variants = TypeFormat.variants.DeAlias
    )

    final class Macros(val c: whitebox.Context) extends MacroBase
    implicit def apply[I]: From[I] = macro Macros.apply[I]
  }

  case object Class extends Base {

    override def format: TypeFormat = TypeFormat(
      hidePackages = true,
      nameOf = TypeFormat.nameOf.Class,
      variants = TypeFormat.variants.DeAlias
    )

    final class Macros(val c: whitebox.Context) extends MacroBase
    implicit def apply[I]: From[I] = macro Macros.apply[I]
  }
}
