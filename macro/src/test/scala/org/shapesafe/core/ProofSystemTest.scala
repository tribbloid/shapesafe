package org.shapesafe.core

import org.shapesafe.BaseSpec

object ProofSystemTest {

  trait Conj

  case class Simple(name: String) extends Conj

  object Sys extends ProofSystem[Conj]

  import Sys._

  case class P0() extends Conj

  object P0 {

    implicit def axiom: P0 =>> Simple = forAll[P0].=>> { p =>
      Simple(p.getClass.toString)
    }
  }

  case class P1[T <: Conj, M](child: T, meta: M)

  object P1 {

    implicit def axiom[S <: Simple, M]: P1[S, M] =>> Simple = forAll[P1[S, M]].=>> { p =>
      Simple(s"${p.getClass.toString} -> ${p.child.name}")
    }

    implicit def theorem[
        T <: Conj,
        S <: Simple,
        M,
        O <: Conj
    ](
        implicit
        lemma1: T |~~ S,
        lemma2: P1[S, M] |-- O
    ): P1[T, M] =>> O = forAll[P1[T, M]].=>> { p =>
      lemma2.valueOf(
        p.copy(lemma1.valueOf(p.child))
      )
    }
  }

}

class ProofSystemTest extends BaseSpec {

  import ProofSystemTest._

//  it("can prove P1") {
//
//    val p1 = P1(P0(), 123)
//
//    Sys.at(p1).entails
//  }
}
