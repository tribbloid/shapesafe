package org.shapesafe.core.fixtures

trait Nat {

  // Basic Operations
  type Add[A <: Nat] <: Nat
  type SubFrom[A <: Nat] <: Nat
  type Mult[A <: Nat] <: Nat
  type Div[A <: Nat] <: Nat

  // Greatest Common Divisor
  type Gcd[A <: Nat] <: Nat

  // Auxiliary operations
  type Pre <: Nat
  type IfZero[T <: Nat, F <: Nat] <: Nat
  type IfLt[A <: Nat, T <: Nat, F <: Nat] <: Nat

  val value: Int
}

object Nat {

  // Natural Number type-class

  // Zero type
  object _0 extends Nat {
    type Add[A <: Nat] = A // 0 + A = A
    type SubFrom[A <: Nat] = A // A - 0 = A
    type Mult[A <: Nat] = _0 // 0 * A = 0
    type Div[A <: Nat] = _0 // 0 / A = 0

    type Gcd[A <: Nat] = A // gcd(0, A) = A

    type Pre = Nothing
    type IfZero[T <: Nat, F <: Nat] = T
    type IfLt[A <: Nat, T <: Nat, F <: Nat] = A#IfZero[F, T]

    val value: Int = 0
  }
  type _0 = _0.type

  // Successor type
  case class ^[N <: Nat]()(
      implicit
      val prev: N
  ) extends Nat {
    type Add[A <: Nat] = ^[N#Add[A]] // S(N) + A = S(N + A)
    type SubFrom[A <: Nat] = N#SubFrom[A#Pre] // A - S(N) = A#Pre - N
    type Mult[A <: Nat] = A#Add[A#Mult[N]] // S(N) * A = A + (A * N)
    type Div[A <: Nat] =
      ^[N]#IfLt[A, _0, ^[A#SubFrom[^[N]]#Div[A]]] // S(N) / A = S((S(N) - A )/ A) if S(N) >= A

    // Euclidean algorithm: gcd(a, b) = gcd(b, a mod b)
    type Gcd[A <: Nat] = GCD[A, ^[N] % A]

    type Pre = N
    type IfZero[T <: Nat, F <: Nat] = F
    type IfLt[A <: Nat, T <: Nat, F <: Nat] = A#IfZero[F, N#IfLt[A#Pre, T, F]]

    val value: Int = prev.value + 1
  }

  implicit def auto0: _0 = _0

  implicit def autoN[N <: Nat](
      implicit
      prev: N
  ): ^[N] = ^()(prev)

  // Basic Operations
  type +[A <: Nat, B <: Nat] = A#Add[B]
  type -[A <: Nat, B <: Nat] = B#SubFrom[A]
  type *[A <: Nat, B <: Nat] = A#Mult[B]
  type /[A <: Nat, B <: Nat] = A#Div[B]
  type %[A <: Nat, B <: Nat] = A - (B * (A / B))

  // Greatest Common Divisor
  type GCD[A <: Nat, B <: Nat] = A#Gcd[B]

  // Type aliases for low numbers
  type _1 = ^[_0]
  type _2 = ^[_1]
  type _3 = ^[_2]
  type _4 = ^[_3]
  type _5 = ^[_4]
  type _6 = ^[_5]
  type _7 = ^[_6]
  type _8 = ^[_7]
  type _9 = ^[_8]
  type _10 = ^[_9]
  type _11 = ^[_10]
  type _12 = ^[_11]
  type _13 = ^[_12]
  type _14 = ^[_13]
  type _15 = ^[_14]
  type _16 = ^[_15]
  type _17 = ^[_16]
  type _18 = ^[_17]
  type _19 = ^[_18]
  type _20 = ^[_19]
  type _21 = ^[_20]
  type _22 = ^[_21]
  type _23 = ^[_22]
  type _24 = ^[_23]
  type _25 = ^[_24]
}
