package org.shapesafe.core

import org.shapesafe.BaseSpec

object ProofSystemSpec {

  trait Nat

  object ProveNat extends ProofSystem.^[Stuff]

  import ProveNat._

  object Nat {

    implicit def axiom0: _0 |- NatStuff[_0] = forAll[_0].=>> { _ =>
      NatStuff(0)
    }
  }

  object _0 extends Nat {}
  type _0 = _0.type

  case class ++[T <: Nat](`--`: T) extends Nat {}

  abstract class Stuff {
    def tier: Int
  }

  case class NatStuff[SRC <: Nat](tier: Int) extends Stuff

  object ++ {

    implicit def axiom1[N <: Nat](
        implicit
        prev: N |- NatStuff[N]
    ): ++[N] |- NatStuff[++[N]] = forAll[++[N]].=>> { nPlus =>
      // TODO: output is not specialised enough
      val n = nPlus.--
      NatStuff(prev(n).tier + 1)
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

    assert(ProveNat.forTerm(_1).construct.tier == 1)
    assert(ProveNat.forTerm(_2).construct.tier == 2)
    assert(ProveNat.forTerm(_3).construct.tier == 3)
    assert(ProveNat.forTerm(_4).construct.tier == 4)
  }

//  it("can refute and ridicule") {
//
//    import ProveNat._
//
//    implicit def bogusAxiom[N <: _2.type](
//        implicit
//        prev: N |- NatStuff[N]
//    ): ++[N] |-\- NatStuff[++[N]] = forAll[++[N]].=\>>()
//
//    ProveNat.forTerm(_1).refute
//    ProveNat.forTerm(_2).refute
//    ProveNat.forTerm(_3).refute
//    ProveNat.forTerm(_4).refute
//  }

  it("can prove using bidirectional lemma") {
    // TODO

  }

  describe("forAll") {

    it("can summon specialised proof") {

      ProveNat.forAll[++[_0]].citing(++.axiom1)
    }
  }
}
