package shapesafe.core.debugging

import shapesafe.Spike
import singleton.ops.RequireMsg

object RequireMsgSpike {

  trait HasM {

    type M = Int
  }

  trait Foo {

    trait FF[T]
  }

  object Foo1 extends Foo {

    implicit def ev1[T <: HasM](
        implicit
        r: RequireMsg[false, "Bad Type: ${T}"]
    ): Seq[FF[T]] = ???

  }

  object Foo2 extends Foo {

    implicit def ev2[T <: HasM](
        implicit
        r: RequireMsg[false, "Bad Type: ${T#M}"]
    ): Set[FF[T]] = ???
  }

  object Foo3 extends Foo {

    implicit def ev3[T <: HasM](
        implicit
        r: RequireMsg[false, "Just Bad Type"]
    ): Set[FF[T]] = ???
  }

  object Foo4 extends Foo {

    implicit def ev4[T <: HasM](
        implicit
        r: Int
    ): Set[FF[T]] = ???
  }
}

class RequireMsgSpike extends Spike {

//  import RequireMsgSpike._

  describe("interpolation of Msg") {

    it("1") {

//      implicitly[Seq[Foo1.FF[HasM]]] //not working: Bad Type:
    }

    it("2") {

//      implicitly[Set[Foo2.FF[HasM]]] //not working: Bad Type:
    }

    it("3") {

//      implicitly[Set[Foo3.FF[HasM]]]
    }

    it("4") {

//      implicitly[Set[Foo4.FF[HasM]]] //not working: corrupted by the previous message
    }
  }
}
