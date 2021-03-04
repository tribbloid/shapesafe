package org.shapesafe.core.axis

import org.shapesafe.BaseSpec
import org.shapesafe.core.arity.Arity
import org.shapesafe.core.arity.ops.ArityOps
import shapeless.HNil

class OldNameUpdaterSystemSpec extends BaseSpec {

  import shapeless.syntax.singleton.mkSingletonOps
  import shapeless.record._

  Record // TODO: don't remove! the IDE may clean up the import erratically

  val ii = "i" ->> Arity(3)

  describe("Appender") {

    val appender = ArityOps.=!=.AppendByName.oldNameUpdater

    describe("can append") {

      describe("old name to a Record that contains identical pair") {

        it(" 1") {

          val existing = ("i" ->> Arity(3)) :: HNil

          val out = appender.apply(existing -> ii)

          out.fields.toString.shouldBe("(i,3:Literal) :: (i,3:Literal) :: HNil")
        }

        it("2") {

          val existing = ("i" ->> Arity(3)) :: ("j" ->> Arity(4)) :: HNil

          val out = appender.apply(existing -> ii)

          out.fields.toString.shouldBe("(i,3:Literal) :: (i,3:Literal) :: (j,4:Literal) :: HNil")
        }
      }
    }

    describe("cannot append if") {

      describe("old name to a Record that contains identical key but different value") {

        it(" 1") {

          val existing = ("i" ->> Arity(4)) :: HNil

          shouldNotCompile(
            """appender.apply(existing -> ii)"""
          )

        }

        it("2") {

          val existing = ("i" ->> Arity(4)) :: ("j" ->> Arity(3)) :: HNil

          shouldNotCompile(
            """val out = appender.apply(existing -> ii)"""
          )
        }

        it("3") {

          val existing = ("j" ->> Arity(3)) :: ("i" ->> Arity(4)) :: HNil

          shouldNotCompile(
            """appender.apply(existing -> ii)"""
          )
        }
      }
    }
  }

  describe("Squasher") {

    val squasher = ArityOps.=!=.SquashByName.oldNameUpdater

    describe("can squash") {

      describe("old name to a Record that contains identical pair") {

        it(" 1") {

          val existing = ("i" ->> Arity(3)) :: HNil

          val out = squasher.apply(existing -> ii)

          out.fields.toString.shouldBe("(i,3:Literal) :: HNil")
        }

        it("2") {

          val existing = ("i" ->> Arity(3)) :: ("j" ->> Arity(4)) :: HNil

          val out = squasher.apply(existing -> ii)

          out.fields.toString.shouldBe("(i,3:Literal) :: (j,4:Literal) :: HNil")
        }
      }
    }
  }
}
