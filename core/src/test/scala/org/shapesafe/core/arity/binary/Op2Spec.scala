package org.shapesafe.core.arity.binary

import com.tribbloids.graph.commons.util.viz.TypeViz
import org.shapesafe.core.arity.{Arity, ArityFixture, LeafArity}
import singleton.ops.impl.Op
import singleton.ops.math.Min
import singleton.ops.{+, OpAuxInt, ToString, XInt, XLong}

class Op2Spec extends ArityFixture {

  describe("can prove") {

    it("arity trivially") {

      val p = a.eval
      p.requireEqual(3)
    }

    it("a + b") {

      val op = a :+ b

      val p = op.eval
      p.requireEqual(7)
    }

    it("a + b + c") {

      val op0 = a :+ b
      val op = op0 :+ c

      val p = op.eval
      p.requireEqual(12)
    }

    it("... in 1 line") {

      val op = a :+ b :+ c

      val p = op.eval
      p.requireEqual(12)
    }

    it("a + b + c + d") {

      val op = a :+ b :+ c :+ Arity._1

      val p = op.eval
      p.requireEqual(13)
    }

    it("b / a") {

      val op = b :/ a

      val p = op.eval
      p.requireEqual(1)
    }

    it("... NOT if b == 0") {

      val op = a :/ Arity._0

      shouldNotCompile {
        "val p = op.asProof"
      }
    }

    it("(a + b - c) / d") {

      val op = (a :+ b :- c) :/ Arity._1

      val p = op.eval
      p.requireEqual(2)
    }
  }

  describe("CANNOT prove") {

    describe("<Operand Without Proof> +") {

      it("a") {

        val op = LeafArity.Unchecked.^ :+ a

        shouldNotCompile(
          "op.asProof"
        )
      }

      it("a + b") {

        val op = LeafArity.Unchecked.^ :+ (a :+ b)

        shouldNotCompile(
          "op.asProof"
        )
      }
    }
  }

  describe("peek") {

//    describe("spike") {
//      it("1") {
//
//        val va = (a :+ b).eval.arity
//
//        {
//          TypeViz[va.Out].should_=:=()
//        }
//      }
//
//      it("2") {
//
////        TypeViz.infer(a).should_=:=()
////        TypeViz.infer(Arity.Unprovable.^).should_=:=()
//
//        {
//
//          val va = (a :+ b).arity
//
//          TypeViz.infer(va).should_=:=()
//          //        TypeViz[va.Out].should_=:=()
//        }
//
//        {
//          val vb = (a :+ Arity.Unprovable.^).arity
//
//          TypeViz.infer(vb).should_=:=()
//        }
//
//        {
//          val vb = (Arity.Unprovable.^ :+ b).arity
//
//          TypeViz.infer(vb).should_=:=()
//        }
//
////        TypeViz[vb.Out].should_=:=()
//      }
//    }

    it("1") {

      shouldNotCompile(
        """(b :+ c).peek""",
        ".*(9)"
      )
    }

    // TODO: add back
//    it("2") {
//
//      (Arity.Unprovable :+ c).peek
//
//      shouldNotCompile(
//        """(Arity.Unprovable :+ c).peek""",
//        "9"
//      )
//    }
  }
}
