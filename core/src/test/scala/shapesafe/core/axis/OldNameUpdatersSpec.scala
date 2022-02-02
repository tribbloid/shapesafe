package shapesafe.core.axis

import shapeless.HNil
import shapesafe.BaseSpec
import shapesafe.core.Ops
import shapesafe.core.arity.Arity

class OldNameUpdatersSpec extends BaseSpec {

  import shapeless.record._
  import shapeless.syntax.singleton.mkSingletonOps

  Record // TODO: don't remove! the IDE may clean up the import erratically

  val ii = "i" ->> Arity(3).arityType

  describe("Appender") {

    val appender = Ops.==!._AppendByName.oldNameUpdater

    describe("can append") {

      describe("old name to a Record that contains identical pair") {

        it(" 1") {

          val existing = ("i" ->> Arity(3).arityType) :: HNil

          val out = appender.apply(existing -> ii)

          out.fields.toString.shouldBe("(i,3:Literal) :: (i,3:Literal) :: HNil")
        }

        it("2") {

          val existing = ("i" ->> Arity(3).arityType) :: ("j" ->> Arity(4).arityType) :: HNil

          val out = appender.apply(existing -> ii)

          out.fields.toString.shouldBe("(i,3:Literal) :: (i,3:Literal) :: (j,4:Literal) :: HNil")
        }
      }
    }

    describe("cannot append if") {

      describe("old name to a Record that contains identical key but different value") {

        it(" 1") {

          val existing = ("i" ->> Arity(4).arityType) :: HNil

          shouldNotCompile(
            """appender.apply(existing -> ii)"""
          )

        }

        it("2") {

          val existing = ("i" ->> Arity(4).arityType) :: ("j" ->> Arity(3).arityType) :: HNil

          shouldNotCompile(
            """val out = appender.apply(existing -> ii)"""
          )
        }

        it("3") {

          val existing = ("j" ->> Arity(3).arityType) :: ("i" ->> Arity(4).arityType) :: HNil

          shouldNotCompile(
            """appender.apply(existing -> ii)"""
          )
        }
      }
    }
  }

  describe("Reducer") {

    val reducer = Ops.==!._ReduceByName.oldNameUpdater

    describe("can squash") {

      describe("old name to a Record that contains identical pair") {

        it(" 1") {

          val existing = ("i" ->> Arity(3).arityType) :: HNil

          val out = reducer.apply(existing -> ii)

          out.fields.toString.shouldBe("(i,3:Literal) :: HNil")
        }

        it("2") {

          val existing = ("i" ->> Arity(3).arityType) :: ("j" ->> Arity(4).arityType) :: HNil

          val out = reducer.apply(existing -> ii)

          out.fields.toString.shouldBe("(i,3:Literal) :: (j,4:Literal) :: HNil")
        }
      }
    }
  }
}
