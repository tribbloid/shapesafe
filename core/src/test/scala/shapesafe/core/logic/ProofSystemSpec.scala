package shapesafe.core.logic

import shapesafe.BaseSpec
import shapesafe.core.fixtures.Peano
import shapesafe.core.util.Peano2ID

object ProofSystemSpec {

  import shapesafe.core.util.Peano2ID._

  object ProveStuffX extends ProveStuff.SuperTheory {}
  object ProveStuffXX extends ProveStuffX.SuperTheory {}

  trait HasSubProve extends Peano2ID {}

  trait HasSubSubProve extends HasSubProve {}
}

class ProofSystemSpec extends BaseSpec {

  import Peano._
  import shapesafe.core.util.Peano2ID._
  import ProveStuff._

  it("can prove recursively") {

    assert(ProveStuff.forTerm(new _1).toGoal[ID[_]].construct.v == 1)
    assert(ProveStuff.forTerm(new _2).toGoal[ID[_]].construct.v == 2)
    assert(ProveStuff.forTerm(new _3).toGoal[ID[_]].construct.v == 3)
    assert(ProveStuff.forTerm(new _4).toGoal[ID[_]].construct.v == 4)
  }

  it("can refute or ridicule") {

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

    for3.prove
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

      case class ID2[SRC <: Peano](v: Int) extends Stuff

      implicit def theorem2[N <: Peano](
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

      case class ID3[SRC <: Peano](v: Int) extends Stuff

      implicit def theorem3[N <: Peano](
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

      object SubProveStuff2 extends ProveStuff.TheoryInSystem

      //      object SubSubProveNat extends SubProvePeano.SubScope with SubProveNat2.SubScope
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
          "Peano2ID.ProveStuff.theory.Proof[Peano.Inc[Peano._0],Peano2ID.ProveStuff.theory.system.Aye[Peano2ID.ID[Peano.Inc[Peano._0.type]]]]"
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
//        "ProofSystemSpec.ID[Peano.^[Peano.^[Peano._0.type]]]"
//      )
//    }
  }
}
