package shapesafe.core.arity

import shapesafe.BaseSpec

class ArityReportersSpec extends BaseSpec {

  describe("can report") {

    it("Literal") {

      val i = Arity(3)

      val m = ArityReasoning._Peek.ForTerm(i.arityType).getMessage
      assert(m.trim == "3")
    }

    it("Derived") {

      val i = (Arity(3) :+ Arity(4)).eval

      val m = ArityReasoning._Peek.ForTerm(i.arityType).getMessage
      assert(m.trim == "7")

    }

    it("Unchecked") {

      val i = Arity.Unchecked

      val m = ArityReasoning._Peek.ForTerm(i.arityType).getMessage
      assert(m.trim == "_UNCHECKED_")
    }

    it("Unprovable") {

      val i = Arity.Unprovable

      val m = ArityReasoning._Peek.ForTerm(i.arityType).getMessage

      assert(m.trim.endsWith("_UNPROVABLE_"))
    }

//    it("Var") {}

    it("Conjecture2") {
      val i = Arity(3) :+ Arity.Unchecked

      val m = ArityReasoning._Peek.ForTerm(i.arityType).getMessage

      assert(
        m.trim ==
          """
          |_UNCHECKED_
          |
          |  :=  3 + _UNCHECKED_
          |""".stripMargin.trim
      )
    }
  }
}
