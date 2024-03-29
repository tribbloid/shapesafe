package shapesafe.core.logic.binary

trait AbelianUnder[D, ??[A <: D, B <: D] <: D, Eye <: D] extends MonoidalUnder[D, ??, Eye] {

  import theory._

  implicit def commutative[
      A <: D,
      B <: D
  ]: Theorem[A ?? B |- (B ?? A)] = forAll[A ?? B].=>> { v =>
    val (ll, rr) = deconstruct(v)
    construct(rr, ll)
  }
}
