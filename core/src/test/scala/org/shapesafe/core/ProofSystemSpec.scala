package org.shapesafe.core

import org.shapesafe.BaseSpec
import org.shapesafe.core.fixtures.Nat
import org.shapesafe.core.util.Nat2ID

object ProofSystemSpec {

  import org.shapesafe.core.util.Nat2ID._

  object SubProveNat extends ProveNat.SubScope
  object SubSubProveNat extends SubProveNat.SubScope

  trait HasSubProve extends Nat2ID {}

  trait HasSubSubProve extends HasSubProve {}
}

class ProofSystemSpec extends BaseSpec {

  import Nat._
  import org.shapesafe.core.util.Nat2ID._

  it("can prove recursively") {

    assert(ProveNat.forTerm(new _1()).toGoal[ID[_]].construct.v == 1)
    assert(ProveNat.forTerm(new _2()).toGoal[ID[_]].construct.v == 2)
    assert(ProveNat.forTerm(new _3()).toGoal[ID[_]].construct.v == 3)
    assert(ProveNat.forTerm(new _4()).toGoal[ID[_]].construct.v == 4)
  }

  it("can refute or ridicule") {

    import ProveNat._

    implicit def bogus[N <: _3]: N |-\- ID[N] = forAll[N].=\>>()

    val for1 = ProveNat.forTerm(new _1).toGoal[ID[_]]
    val for2 = ProveNat.forTerm(new _2).toGoal[ID[_]]
    val for3 = ProveNat.forTerm(new _3).toGoal[ID[_]]
    val for4 = ProveNat.forTerm(new _4).toGoal[ID[_]]

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

    it("in a sub-scope") {

      import org.shapesafe.core.ProofSystemSpec.SubProveNat._

      case class ID2[SRC <: Nat](v: Int) extends Stuff

      implicit def theorem2[N <: Nat](
          // depends on theorem in Parent scope
          implicit
          lemma1: ProveNat.|-[N, ID[N]]
      ): N |- ID2[N] = forAll[N].=>> { nPlus =>
        ID2(nPlus.value)
      }

      val s2 = ProveNat
        .forTerm(new _1)
        .toGoal[ID2[_]]
        .construct

      assert(s2 == ID2(1))
    }

    it("in a sub-sub-scope") {

      import org.shapesafe.core.ProofSystemSpec.SubSubProveNat._

      case class ID3[SRC <: Nat](v: Int) extends Stuff

      implicit def theorem3[N <: Nat](
          // depends on theorem in Parent scope
          implicit
          lemma1: ProveNat.|-[N, ID[N]]
      ): N |- ID3[N] = forAll[N].=>> { nPlus =>
        ID3(nPlus.value)
      }

      // TODO: the above 2 imports (with renaming) of implicit should totally be automatic
      //  unfortunately, the current generation of compiler doesn't have the capability of
      //  resolving anonymous implicit function or renaming conflicting ones
      //  this has to be introduced after scala 3

      val s2 = ProveNat
        .forTerm(new _1)
        .toGoal[ID3[_]]
        .construct

      assert(s2 == ID3(1))
    }

    ignore(" ... with diamond type hierarchy") {

      object SubProveNat2 extends ProveNat.SubScope

      //      object SubSubProveNat extends SubProveNat.SubScope with SubProveNat2.SubScope
      // TODO: at this moment, compiler says "class SubScope is inherited twice"
    }
  }

  describe("Tactic") {

    it("can cite once") {

      val v = ProveNat
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
          "Nat2ID.ProveNat.Proof[Nat.Inc[Nat._0],Nat2ID.ProveNat.system.Aye[Nat2ID.ID[Nat.Inc[Nat._0.type]]]]"
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
