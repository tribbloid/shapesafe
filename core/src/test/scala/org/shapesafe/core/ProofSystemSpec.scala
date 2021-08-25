package org.shapesafe.core

import org.shapesafe.BaseSpec
import org.shapesafe.core.fixtures.Nat

object ProofSystemSpec {

  abstract class Stuff {
    def tier: Int
  }

  object Stuff {

    import Nat._
    import ProveNat._

    implicit def axiom0: _0 |- ID[_0] = forAll[_0].=>> { _ =>
      ID(0)
    }

    implicit def theorem0[N <: Nat](
        implicit
        prev: N |- ID[N]
    ): ^[N] |- ID[^[N]] = forAll[^[N]].=>> { nPlus =>
      val p = nPlus.prev
      ID(prev.apply(p).tier + 1)
    }
  }

  case class ID[SRC <: Nat](tier: Int) extends Stuff

  case class Fibb[SRC <: Nat](tier: Int) extends Stuff

  object ProveNat extends ProofSystem.^[Stuff] with NaturalDeductionMixin
}

class ProofSystemSpec extends BaseSpec {

  import Nat._
  import ProofSystemSpec._

  it("can prove recursively") {

    assert(ProveNat.forTerm(new _1()).construct.tier == 1)
    assert(ProveNat.forTerm(new _2()).construct.tier == 2)
    assert(ProveNat.forTerm(new _3()).construct.tier == 3)
    assert(ProveNat.forTerm(new _4()).construct.tier == 4)
  }

  it("can refute or ridicule") {

    import ProveNat._

    implicit def bogus[N <: _2](
        implicit
        prev: N |- ID[N]
    ): ^[N] |-\- ID[^[N]] = forAll[^[N]].=\>>()

    val for1 = ProveNat.forTerm(new _1)
    val for2 = ProveNat.forTerm(new _2)
    val for3 = ProveNat.forTerm(new _3)
    val for4 = ProveNat.forTerm(new _4)

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

    object SubProveNat extends ProveNat.SubScope

    it("in a sub-scope") {

      import SubProveNat._

      case class ID2[SRC <: Nat](tier: Int) extends Stuff

      implicit def axiom2[N <: Nat]: ID[^[N]] |- ID2[^[N]] = forAll[ID[^[N]]].=>> { id =>
        ID2(id.tier)
      }

      val s2 = ProveNat
        .forTerm(new _1)
        .toGoal[ID2[_]]
        .construct

      assert(s2 == ID2(1))
    }

    object SubSubProveNat extends SubProveNat.SubScope

    it("in a sub-sub-scope") {

      import SubSubProveNat._

      case class ID3[SRC <: Nat](tier: Int) extends Stuff

      implicit def theorem3[N <: Nat](
          implicit
          prev: ProveNat.|-[N, ID[N]] // TODO: can this be infixed?
      ): ^[N] |- ID3[^[N]] = forAll[^[N]].=>> { nPlus =>
        val n = nPlus.prev
        ID3[^[N]](prev(n).tier + 2)
      }

      import SubProveNat.{coerciveUpcast => up1}
      import ProveNat.{coerciveUpcast => u2}
      // TODO: the above 2 imports (with renaming) of implicit should totally be automatic
      //  unfortunately, the current generation of compiler doesn't have the capability of
      //  resolving anonymous implicit function or renaming conflicting ones
      //  this has to be introduced after scala 3

      val s2 = ProveNat
        .forTerm(new _1)
        .toGoal[ID3[_]]
        .construct

      assert(s2 == ID3(2))
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
        .useTactic { tt =>
          tt
            .cite(Stuff.theorem0(Stuff.axiom0))
        }

      TypeVizShort[v.SubGoal].typeStr.shouldBe(
        "ProofSystemSpec.ID[Nat.^[Nat._0.type]]"
      )
    }

    it("... twice") {

//      val v = ProveNat
//        .forAll[_1.Self]
//        .cite(Nat.axiom1)
//
//      TypeVizShort[v.SubGoal].typeStr.shouldBe(
//        "ProofSystemSpec.S1[ProofSystemSpec.++[ProofSystemSpec._0.type]]"
//      )

    }
  }
}
