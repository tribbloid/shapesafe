package org.shapesafe.core.debugging

import com.tribbloids.graph.commons.util.reflect.format.FormatOvrd.{~~, Only}
import com.tribbloids.graph.commons.util.reflect.format.Formats.KindName

object OvrdUtil {

  type Ovrd[T <: CanPeek] = T#_Ovrd with KindName[T]

  object Ovrd {

    trait Infix[A <: CanPeek, S, B <: CanPeek] extends (Ovrd[A] ~~ Only[S] ~~ Ovrd[B])

    trait :<<-[A <: CanPeek, B <: CanPeek] extends Infix[A, ":<<-", B]
    trait ><[A <: CanPeek, B <: CanPeek] extends Infix[A, "><", B]
  }
}
