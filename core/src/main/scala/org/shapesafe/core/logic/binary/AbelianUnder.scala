package org.shapesafe.core.logic.binary

// TODO: I don't like this name, should be "CommutativeUnder"
trait AbelianUnder[??[A, B], Eye] extends MonoidalUnder[??, Eye] {

  import prove._

  implicit def commutative[
      A,
      B
  ]: Axiom[A ?? B |- (B ?? A)] = forAll[A ?? B].=>> { v =>
    val (ll, rr) = deconstruct(v)
    construct(rr, ll)
  }
}
