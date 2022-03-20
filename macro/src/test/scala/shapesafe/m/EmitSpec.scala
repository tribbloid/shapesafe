package shapesafe.m

import ai.acyclic.graph.commons.testlib.BaseSpec

class EmitSpec extends BaseSpec {

  import EmitSpec._

  describe("Error") {

    implicit def direct(
        implicit
        emit: Emit.Error["Test123"]
    ): A = ???

    implicit def indirect(
        implicit
        a: A
    ): B = ???

    it("directly") {

      shouldNotCompile(
        """direct""",
        "Test123"
      )
    }

    it("indirectly") {

      shouldNotCompile(
        """indirect""",
        "Test123"
      )
    }

    it(" ... as a fallback") {

      implicit def tryFirst(
          implicit
          ev: Int
      ): A = ???

      shouldNotCompile(
        """indirect""",
        "Test123"
      )
    }
  }
}

object EmitSpec {

  trait A
  trait B
}
