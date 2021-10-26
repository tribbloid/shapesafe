package org.shapesafe.core.logic.binary

import org.shapesafe.core.logic.HasTheory

trait GroupAxioms[D, :+[A <: D, B <: D] <: D, _0 <: D] extends HasTheory {

  trait Group_+ extends AbelianUnder[D, :+, _0] {

    final override val theory: GroupAxioms.this.theory.type = GroupAxioms.this.theory
  }
}
