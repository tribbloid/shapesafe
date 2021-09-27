package org.shapesafe.core

/**
  * This trait forms the backbone of compile-time reasoning and should reflect our best effort in reproducing
  * Curry-Howard isomorphism with scala compiler (regardless of how ill-suited it is), expecting drastic changes upon
  * improvement over its implicit search algorithm
  */
trait ProofSystem extends AxiomaticSystem with ProofScope { // TODO: no IUB?

  final type System = this.type
  final val system = this

  abstract class SubScopeInSystem extends ProofScope {

    final override type OUB = ProofSystem.this.OUB
  }

//  object ScopeInSystem {
//
//    type Aux = ProofScope {
//      val system: ProofSystem.this.type
//    }
//  }

//  implicitly[ProofSystem.this.type <:< ScopeInSystem.Aux]

//  implicitly[SubScope <:< ScopeInSystem.Aux]

//  implicit def coerce1[GG <: ScopeInSystem with Singleton, SS <: GG#SubScope with Singleton, I, P <: Proposition](
//      implicit
//      moreSpecific: SS#Proof[I, P]
//  ) = {
//
//    val specificScope = moreSpecific.scope: SS
//    val generalScope = specificScope.outer
//
//    generalScope.forAll[I].=>> { v =>
//      moreSpecific
//        .consequentFor(v)
//        .asInstanceOf[generalScope.OUB]
//    }
//  }

//  implicit def coerciveUpcast[S <: ScopeInSystem, I, P <: system.Consequent](
//      implicit
//      proofInSubScope: S#SubScope#Proof[I, P]
//  ): S#Proof[I, P] = { (v: I) =>
//    proofInSubScope.consequentFor(v)
//  }

//  implicit def coerciveUpcast[GG <: ProofScope with Singleton, I, P <: GG#System#Proposition](
//      implicit
//      gg: GG,
//      proofInSubScope: GG#SubScope#Proof[I, P]
//  ): GG#Proof[I, P] = {
//    gg.coerciveUpcastFromSubScopeImpl(proofInSubScope.asInstanceOf[gg.SubScope#Proof[I, P]])
//
//  }
}

object ProofSystem {

  type Aux[T] = ProofSystem {
    type OUB = T
  }

  trait ^[T] extends ProofSystem {
    final override type OUB = T
  }
}
