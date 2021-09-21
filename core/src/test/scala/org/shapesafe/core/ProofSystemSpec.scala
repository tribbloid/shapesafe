package org.shapesafe.core

import org.shapesafe.BaseSpec

object ProofSystemSpec {

  trait Nat {

    type Self <: Nat
  }

  abstract class Stuff {
    def tier: Int
  }

  case class S1[SRC <: Nat](tier: Int) extends Stuff

  object ProveNat extends ProofSystem.^[Stuff]

  import ProveNat._

  object Nat {

    implicit def axiom0: _0 |- S1[_0] = forAll[_0].=>> { _ =>
      S1(0)
    }
  }

  case object _0 extends Nat {

    type Self = _0
  }
  type _0 = _0.type

  case class ++[T <: Nat](`--`: T) extends Nat {

    type Self = ++[T]
  }

  object ++ {

    implicit def axiom1[N <: Nat](
        implicit
        prev: N |- S1[N]
    ): ++[N] |- S1[++[N]] = forAll[++[N]].=>> { nPlus =>
      // TODO: output is not specialised enough
      val n = nPlus.--
      S1(prev(n).tier + 1)
    }
  }

  val _1 = ++(_0)
  val _2 = ++(_1)
  val _3 = ++(_2)
  val _4 = ++(_3)
}

class ProofSystemSpec extends BaseSpec {

  import ProofSystemSpec._

  it("can prove recursively") {

    assert(ProveNat.forTerm(_1).construct == S1(1))
    assert(ProveNat.forTerm(_2).construct == S1(2))
    assert(ProveNat.forTerm(_3).construct == S1(3))
    assert(ProveNat.forTerm(_4).construct == S1(4))
  }

  it("can refute or ridicule") {

    import ProveNat._

    implicit def bogusAxiom[N <: _2.Self](
        implicit
        prev: N |- S1[N]
    ): ++[N] |-\- S1[++[N]] = forAll[++[N]].=\>>()

    this.shouldNotCompile(
      "ProveNat.forTerm(_1).refute"
    )
    this.shouldNotCompile(
      "ProveNat.forTerm(_2).refute"
    )
    this.shouldNotCompile(
      "ProveNat.forTerm(_4).refute"
    )

    val for3 = ProveNat.forTerm(_3)
    for3.refute
    for3.ridicule

  }

  it("can prove using bidirectional lemma") {
    // TODO

  }

  describe("coercive upcasting for proof") {

    case class S2[SRC <: Nat](tier: Int) extends Stuff
    case class S3[SRC <: Nat](tier: Int) extends Stuff

    object SubProveNat extends ProveNat.SubScope

    object SubSubProvenat extends SubProveNat.SubScope

    it("in a sub-scope") {

      import SubProveNat._

      implicit def axiom2[N <: Nat](
          implicit
          prev: ProveNat.|-[N, S1[N]] // TODO: can this be infixed?
      ): ++[N] |- S2[++[N]] = forAll[++[N]].=>> { nPlus =>
        val n = nPlus.--
        S2(prev(n).tier + 1)
      }

      // TODO: remove
//      TypeVizShort[_1.Self].toString().shouldBe()

      val s2 = ProveNat
        .forTerm(_1)
        .toGoal[S2[_1.Self]]
        .construct

      assert(s2 == S2(1))
    }

    it("in a sub-sub-scope") {}

    it(" ... with diamond Heyting algebra") {}
  }

  describe("forAll") {

    it("can cite specific proof") {

      val v = ProveNat.forAll[++[_0]].citing(++.axiom1)

      TypeVizShort[v.SubGoal].typeStr.shouldBe(
        "ProofSystemSpec.S1[ProofSystemSpec.++[ProofSystemSpec._0]]"
      )

    }

    it("... twice") {}
  }
}
