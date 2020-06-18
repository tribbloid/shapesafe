package edu.umontreal.kotlingrad.shapesafe.`macro`.arity.binary

import edu.umontreal.kotlingrad.shapesafe.BaseSpec
import edu.umontreal.kotlingrad.shapesafe.`macro`.arity.Arity
import edu.umontreal.kotlingrad.shapesafe.`macro`.arity.Proof.Invar
import shapeless.Lazy
import singleton.ops.+

class Op2Spec extends BaseSpec {

  import edu.umontreal.kotlingrad.shapesafe.`macro`.arity.binary.Op2._

  describe("can summon") {

    implicit val a = Arity(3)
    type A = a.Out

    implicit val b = Arity(4)
    type B = b.Out

    implicit val c = Arity(5)
    type C = c.Out

    //    case class Wrapper[T <: Arity]()(implicit v: T) {
    //
    //
    //    }

    it("a + b") {

      type Sum = A ^+ B
      implicit val v = implicitly[Sum]

      //      val sum = Op2.const(a, b, implicitly[a.Single + b.Single])

      //      implicit val shouldBe = Arity.FromLiteral(7)
      //      type ShouldBe = shouldBe.type
      //
      //      implicitly[Sum MayEqual ShouldBe](MayEqual.const[Witness.`7`.T, Witness.`7`.T])

      //      implicitly[sum MayEqual ref]

      //      val result = implicitly[a.type Arity_+ b.type]
      //
      //      val w = Wrapper[a.type Arity_+ b.type ]
      //
      //      w.
      //
      //
      //
      //      val v2: ArityLike.Const[_] = result

      //      println(result.Out.internal)
    }

    it("a / b") {

      type R = A ^/ B
      implicit val r = implicitly[R]

    }

    it("... NOT if b == 0") {

      type R = A ^/ Arity._0

      shouldNotCompile {
        "implicit val r = implicitly[R]"
      }
    }

    it("a + b + c") {

      val v1 = implicitly[A ^+ B]

      implicit val r = {

        val domain: InvarDomain[a.SS, b.SS] = {

          //          ProveConst.summon(a, b)

          implicitly[InvarDomain[a.SS, b.SS]]
        }

        val op2: (A ^+ B) with Invar[a.SS + b.SS] = {

          //          Op2.const(prove, implicitly[a.SS + b.SS])

          //          Op2.const[a.SS, b.SS, +]

          //          Op2.const

          implicitly[(A ^+ B) with Invar[a.SS + b.SS]]
        }

//        type RC = (A ^+ B ^+ C) with Const[a.SS + b.SS + c.SS]
//        implicit val rc = implicitly[RC]

        val domain2: InvarDomain[a.SS + b.SS, c.SS] = {

          val c2 = implicitly[Invar[c.SS]]

//          val c3 = InvarDomain.summon[a.SS + b.SS, c.SS] //((c1), (c2))

          val c3 = implicitly[InvarDomain[a.SS + b.SS, c.SS]] //((c1), (c2))
          c3
        }

        type R = (A ^+ B ^+ C)

        val r: (Invar[a.SS + b.SS] ^+ Invar[c.SS]) with Invar[a.SS + b.SS + c.SS] =
          Op2.invar(domain2, implicitly[(a.SS + b.SS) + c.SS])

//        implicit val r: R = {
//
//          val domain: ConstDomain[op2A.SS, c.SS] = implicitly
//
////          Op2.const[op2A.SS, c.SS, +]
//
////          implicitly[R]
//
//          ???
//        }
      }

//      it("a + b - c") {
//
//        val op3: A ^+ B ^- C = {
//
//          implicitly
//        }
//
////        val op2X = implicitly[(A ^+ B) with Const[a.SS + b.SS]] //(op2)
//
////        val r2a = implicitly[A ^]
//
////        implicit val r2 = implicitly[(A ^+ B) with Const[a.SS + b.SS]](op2)
////        implicit val r3 = implicitly[C](c)
////
////        val consts: ProveConst[a.w.T + b.w.T, c.w.T] = ProveConst.summon(r2, r3)
////
////        Op2.const(consts, implicitly[a.Single + b.Single - c.Single])
//      }

      //      implicit val r = implicitly[R]
    }

    it("(a + b - c) / d") {

      //      type R = (A Arity_+ B Arity_- C) Arity_/ Arity._1
      //      implicit val r = implicitly[R]
    }
  }
}
