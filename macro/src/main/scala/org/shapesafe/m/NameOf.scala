package org.shapesafe.m

import com.tribbloids.graph.commons.util.reflect.TypeFormat
import shapeless.Witness

import scala.language.experimental.macros
import scala.reflect.macros.whitebox

object NameOf {

  trait Impl extends MWithReflection {

    def format: TypeFormat

    import c.universe._

    def apply[A: WeakTypeTag]: Tree = {

      val tt: Type = weakTypeOf[A]
      val str = refl.TypeView(tt).Display(format).base

      q"shapeless.Witness.mkWitness[$str]($str)"
    }
  }

  object TypeConstructor {

    def apply[I]: Witness.Lt[String] = macro Macro.apply[I]

    final class Macro(val c: whitebox.Context) extends Impl {

      override def format: TypeFormat = TypeFormat(
        hidePackages = true,
        nameOf = TypeFormat.nameOf.TypeConstructor,
        variants = TypeFormat.variants.Dealias
      )
    }
  }

  object Class {

    def apply[I]: Witness.Lt[String] = macro Macro.apply[I]

    final class Macro(val c: whitebox.Context) extends Impl {

      override def format: TypeFormat = TypeFormat(
        hidePackages = true,
        nameOf = TypeFormat.nameOf.Class,
        variants = TypeFormat.variants.Dealias
      )
    }
  }

}
