package org.shapesafe.core.fixtures

import org.shapesafe.core.ProofSystem

// courtesy of https://github.com/Joe-Edwards/type-arithmetic
object NatNumAxioms {

  // Natural Number type-class
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
  }

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
  }
  type _0 = _0.type

  // Successor type
  class S[N <: Nat] extends Nat {
    type Add[A <: Nat] = S[N#Add[A]] // S(N) + A = S(N + A)
    type SubFrom[A <: Nat] = N#SubFrom[A#Pre] // A - S(N) = A#Pre - N
    type Mult[A <: Nat] = A#Add[A#Mult[N]] // S(N) * A = A + (A * N)
    type Div[A <: Nat] = S[N]#IfLt[A, _0, S[A#SubFrom[S[N]]#Div[A]]] // S(N) / A = S((S(N) - A )/ A) if S(N) >= A

    // Euclidean algorithm: gcd(a, b) = gcd(b, a mod b)
    type Gcd[A <: Nat] = gcd[A, S[N] % A]

    type Pre = N
    type IfZero[T <: Nat, F <: Nat] = F
    type IfLt[A <: Nat, T <: Nat, F <: Nat] = A#IfZero[F, N#IfLt[A#Pre, T, F]]
  }

  // Basic Operations
  type +[A <: Nat, B <: Nat] = A#Add[B]
  type -[A <: Nat, B <: Nat] = B#SubFrom[A]
  type *[A <: Nat, B <: Nat] = A#Mult[B]
  type /[A <: Nat, B <: Nat] = A#Div[B]
  type %[A <: Nat, B <: Nat] = A - (B * (A / B))

  // Greatest Common Divisor
  type gcd[A <: Nat, B <: Nat] = A#Gcd[B]

  // Type aliases for low numbers
  type _1 = S[_0]
  type _2 = S[_1]
  type _3 = S[_2]
  type _4 = S[_3]
  type _5 = S[_4]
  type _6 = S[_5]
  type _7 = S[_6]
  type _8 = S[_7]
  type _9 = S[_8]
  type _10 = S[_9]
  type _11 = S[_10]
  type _12 = S[_11]
  type _13 = S[_12]
  type _14 = S[_13]
  type _15 = S[_14]
  type _16 = S[_15]
  type _17 = S[_16]
  type _18 = S[_17]
  type _19 = S[_18]
  type _20 = S[_19]
  type _21 = S[_20]
  type _22 = S[_21]
  type _23 = S[_22]
  type _24 = S[_23]
  type _25 = S[_24]

  object Prove extends ProofSystem.^[Nat]

}
