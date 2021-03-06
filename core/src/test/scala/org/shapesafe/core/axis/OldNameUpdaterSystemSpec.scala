package org.shapesafe.core.axis

import org.shapesafe.BaseSpec
import org.shapesafe.core.arity.{Arity, ArityAPI}
import org.shapesafe.core.arity.ops.ArityOps
import shapeless.HNil

class OldNameUpdaterSystemSpec extends BaseSpec {

  import shapeless.record._
  import shapeless.syntax.singleton.mkSingletonOps

  Record // TODO: don't remove! the IDE may clean up the import erratically

  val ii = "i" ->> Arity(3).arity

  describe("Appender") {

    val appender = ArityOps.==!._AppendByName.oldNameUpdater

    describe("can append") {

      describe("old name to a Record that contains identical pair") {

        it(" 1") {

          val existing = ("i" ->> Arity(3).arity) :: HNil

          val out = appender.apply(existing -> ii)

          out.fields.toString.shouldBe("(i,3:Literal) :: (i,3:Literal) :: HNil")
        }

        it("2") {

          val existing = ("i" ->> Arity(3).arity) :: ("j" ->> Arity(4).arity) :: HNil

          val out = appender.apply(existing -> ii)

          out.fields.toString.shouldBe("(i,3:Literal) :: (i,3:Literal) :: (j,4:Literal) :: HNil")
        }
      }
    }

    describe("cannot append if") {

      describe("old name to a Record that contains identical key but different value") {

        it(" 1") {

          val existing = ("i" ->> Arity(4).arity) :: HNil

          shouldNotCompile(
            """appender.apply(existing -> ii)"""
          )

        }

        it("2") {

          val existing = ("i" ->> Arity(4).arity) :: ("j" ->> Arity(3).arity) :: HNil

          shouldNotCompile(
            """val out = appender.apply(existing -> ii)"""
          )
        }

        it("3") {

          val existing = ("j" ->> Arity(3).arity) :: ("i" ->> Arity(4).arity) :: HNil

          shouldNotCompile(
            """appender.apply(existing -> ii)"""
          )
        }
      }
    }
  }

  describe("Squasher") {

    val squasher = ArityOps.==!._SquashByName.oldNameUpdater

    describe("can squash") {

      describe("old name to a Record that contains identical pair") {

        it(" 1") {

          val existing = ("i" ->> Arity(3).arity) :: HNil

          val out = squasher.apply(existing -> ii)

          out.fields.toString.shouldBe("(i,3:Literal) :: HNil")
        }

        it("2") {

          val existing = ("i" ->> Arity(3).arity) :: ("j" ->> Arity(4).arity) :: HNil

          val out = squasher.apply(existing -> ii)

          out.fields.toString.shouldBe("(i,3:Literal) :: (j,4:Literal) :: HNil")
        }
      }
    }
  }
}
