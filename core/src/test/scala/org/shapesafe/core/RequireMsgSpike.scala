package org.shapesafe.core

import singleton.ops.RequireMsg

object RequireMsgSpike {

  trait HasM {

    type M = Int
  }

  trait Foo[T]

  object Foo {

    implicit def ev[T <: HasM](
        implicit
        r: RequireMsg[false, "Bad Type: ${T#M}"]
    ): Foo[T] = ???
  }
//  implicitly[Foo[HasM]] //error: Bad Type:
}
