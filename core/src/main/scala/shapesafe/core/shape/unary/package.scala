package shapesafe.core.shape

package object unary {

  /*
  you may find a lot of classes with type constructors like:

  XXX[
    S1 <: Shape
  ](
    s1: S1 with Shape
  )

  this weird type signature is necessary to summon implicit unboxing function of ShapeAPI

  without the `with Shape` part, scala compiler will be too dumb to invoke the unboxing automatically
   */

}
