package org.shapesafe.core.debugging

import org.shapesafe.Spike
import singleton.ops.RequireMsg

object RequireMsgSpike {

  trait HasM {

    type M = Int
  }

  trait Foo[T]

  object Foo {

    implicit def ev1[T <: HasM](
        implicit
        r: RequireMsg[false, "Bad Type: ${T}"]
    ): Seq[Foo[T]] = ???

    implicit def ev2[T <: HasM](
        implicit
        r: RequireMsg[false, "Bad Type: ${T#M}"]
    ): Set[Foo[T]] = ???
  }
}

class RequireMsgSpike extends Spike {

  describe("interpolation of Msg") {

    it("1") {

//      implicitly[Seq[Foo[HasM]]] //not working: Bad Type:
    }

    it("2") {

//      implicitly[Set[Foo[HasM]]] //not working: Bad Type:
    }
  }
}
