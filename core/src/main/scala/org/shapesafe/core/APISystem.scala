package org.shapesafe.core

import scala.language.implicitConversions

/**
  * A class of which each instance contains an inner dependent type and an instance of such type, generally used for:
  *
  * - functions where generic type parameters are impossible or too verbose to define
  * (e.g. the native implicit scope of a generic type parameter cannot be included automatically)
  *
  * - instance of which peer type needs to be used
  *
  * defined to circumvent the lack of "peer type" or "tightest non-singleton self-type" in scala.
  *
  * using singleton type `this.type` directly is incompatible with invariant type parameters and causes fuzzy error messages
  *
  * using whitebox macro maybe helpful (see NonSingletonTUB in macro package) which I'm skeptical, it makes dotty upgrade much harder
  *
  * other alternatives are F-bounded polymorphism and type classes, both of which are too verbose/not powerful enough
  */
trait APISystem {

  type Bound

  // TODO: this is not used at the moment, ArityAPI may extend ShapeAPI which cause diamond inheritance problem of the same member
  trait API {

    type Inner <: Bound
    def inner: Inner
  }

  object API {

    type Aux[I <: Bound] = API { type Internal = I }
  }

  implicit final def unbox[T <: API](v: T): v.Inner = v.inner
//  implicit final def box[T <: API](v: T): API.Aux[Bound] = create(v)

  def create[I <: Bound](internal: I): API.Aux[I]

  trait APICompanion {}
}

object APISystem {}
