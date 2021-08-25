package org.shapesafe.core

import org.shapesafe.BaseSpec
import org.shapesafe.core.fixtures.Nat
import org.shapesafe.core.util.Nat2ID

object ProofSystemSpec {

  import org.shapesafe.core.util.Nat2ID._

  object ProveStuffX extends ProveStuff.SuperTheory {}
  object ProveStuffXX extends ProveStuffX.SuperTheory {}

  trait HasSubProve extends Nat2ID {}

  trait HasSubSubProve extends HasSubProve {}
}

class ProofSystemSpec extends BaseSpec {

  import Nat._
  import org.shapesafe.core.util.Nat2ID._

  it("can prove recursively") {

    assert(ProveStuff.forTerm(new _1()).toGoal[ID[_]].construct.v == 1)
    assert(ProveStuff.forTerm(new _2()).toGoal[ID[_]].construct.v == 2)
    assert(ProveStuff.forTerm(new _3()).toGoal[ID[_]].construct.v == 3)
    assert(ProveStuff.forTerm(new _4()).toGoal[ID[_]].construct.v == 4)
  }

  it("can refute or ridicule") {

    import ProveStuff._

    implicit def bogus[N <: _3]: N |-\- ID[N] = forAll[N].=\>>()

    val for1 = ProveStuff.forTerm(new _1).toGoal[ID[_]]
    val for2 = ProveStuff.forTerm(new _2).toGoal[ID[_]]
    val for3 = ProveStuff.forTerm(new _3).toGoal[ID[_]]
    val for4 = ProveStuff.forTerm(new _4).toGoal[ID[_]]

    this.shouldNotCompile(
      "for1.refute"
    )
    this.shouldNotCompile(
      "for2.refute"
    )

    for3.refute
    for3.ridicule

    this.shouldNotCompile(
      "for4.refute"
    )
  }

  it("can prove using bidirectional lemma") {
    // TODO

  }

  describe("coercive upcasting of proof") {

    it("in a sub-thoery") {

      import org.shapesafe.core.ProofSystemSpec.ProveStuffX._

      case class ID2[SRC <: Nat](v: Int) extends Stuff

      implicit def theorem2[N <: Nat](
          // depends on theorem in Parent scope
          implicit
          lemma1: ProveStuff.|-[N, ID[N]]
      ): N |- ID2[N] = forAll[N].=>> { nPlus =>
        ID2(nPlus.value)
      }

      val s2 = forTerm(new _1)
        .toGoal[ID2[_]]
        .construct

      assert(s2 == ID2(1))
    }

    it("in a sub-sub-theory") {

      import org.shapesafe.core.ProofSystemSpec.ProveStuffXX._

      case class ID3[SRC <: Nat](v: Int) extends Stuff

      implicit def theorem3[N <: Nat](
          // depends on theorem in Parent scope
          implicit
          lemma1: ProveStuff.|-[N, ID[N]]
      ): N |- ID3[N] = forAll[N].=>> { nPlus =>
        ID3(nPlus.value)
      }

      val s2 = forTerm(new _1)
        .toGoal[ID3[_]]
        .construct

      assert(s2 == ID3(1))
    }

    ignore(" ... with diamond type hierarchy") {

      object SubProveStuff2 extends ProveStuff.ExtensionLike

      //      object SubSubProveNat extends SubProveNat.SubScope with SubProveNat2.SubScope
      // TODO: at this moment, compiler says "class SubScope is inherited twice"
    }
  }

  describe("Tactic") {

    it("can cite once") {

      val v = ProveStuff
        .forAll[_1]
        .toGoal[ID[_]]
        .useTactic { tt =>
          tt
            .cite(Stuff.idTheorem)
        }
        .fulfil

      TypeVizShort
        .infer(v)
        .typeStr
        .shouldBe(
          "Nat2ID.ProveStuff.Proof[Nat.Inc[Nat._0],Nat2ID.ProveStuff.system.Aye[Nat2ID.ID[Nat.Inc[Nat._0.type]]]]"
        )
    }

//    it("... twice") {
//
//      val v = ProveNat
//        .forAll[_1]
//        .useTactic { tt =>
//          tt
//            .cite(Stuff.idTheorem)
//            .cite(Stuff.theorem1)
//        }
//
//      TypeVizShort[v.SubGoal].typeStr.shouldBe(
//        "ProofSystemSpec.ID[Nat.^[Nat.^[Nat._0.type]]]"
//      )
//    }
  }
}
