package shapesafe.core.fixtures

trait Peano {

  // Basic Operations
  type Add[A <: Peano] <: Peano
  type SubFrom[A <: Peano] <: Peano
  type Mult[A <: Peano] <: Peano
  type Div[A <: Peano] <: Peano

  // Greatest Common Divisor
  type Gcd[A <: Peano] <: Peano

  // Auxiliary operations
  type Pre <: Peano
  type IfZero[T <: Peano, F <: Peano] <: Peano
  type IfLt[A <: Peano, T <: Peano, F <: Peano] <: Peano

  val value: Int
}

object Peano {

  // Natural Number type-class

  // Zero type
  object _0 extends Peano {

    type Add[A <: Peano] = A // 0 + A = A
    type SubFrom[A <: Peano] = A // A - 0 = A
    type Mult[A <: Peano] = _0 // 0 * A = 0
    type Div[A <: Peano] = _0 // 0 / A = 0

    type Gcd[A <: Peano] = A // gcd(0, A) = A

    type Pre = Nothing
    type IfZero[T <: Peano, F <: Peano] = T
    type IfLt[A <: Peano, T <: Peano, F <: Peano] = A#IfZero[F, T]

    val value: Int = 0
  }
  type _0 = _0.type

  // Successor type
  case class Inc[N <: Peano]()(
      implicit
      val prev: N
  ) extends Peano {

    type Add[A <: Peano] = Inc[N#Add[A]] // S(N) + A = S(N + A)
    type SubFrom[A <: Peano] = N#SubFrom[A#Pre] // A - S(N) = A#Pre - N
    type Mult[A <: Peano] = A#Add[A#Mult[N]] // S(N) * A = A + (A * N)
    type Div[A <: Peano] =
      Inc[N]#IfLt[A, _0, Inc[A#SubFrom[Inc[N]]#Div[A]]] // S(N) / A = S((S(N) - A )/ A) if S(N) >= A

    // Euclidean algorithm: gcd(a, b) = gcd(b, a mod b)
    type Gcd[A <: Peano] = GCD[A, Inc[N] % A]

    type Pre = N
    type IfZero[T <: Peano, F <: Peano] = F
    type IfLt[A <: Peano, T <: Peano, F <: Peano] = A#IfZero[F, N#IfLt[A#Pre, T, F]]

    val value: Int = prev.value + 1
  }

  implicit def auto0: _0 = _0

  implicit def autoN[N <: Peano](
      implicit
      prev: N
  ): Inc[N] = Inc()(prev)

  // Basic Operations
  type +[A <: Peano, B <: Peano] = A#Add[B]
  type -[A <: Peano, B <: Peano] = B#SubFrom[A]
  type *[A <: Peano, B <: Peano] = A#Mult[B]
  type /[A <: Peano, B <: Peano] = A#Div[B]
  type %[A <: Peano, B <: Peano] = A - (B * (A / B))

  // Greatest Common Divisor
  type GCD[A <: Peano, B <: Peano] = A#Gcd[B]

  // Type aliases for low numbers
  type _1 = Inc[_0]
  type _2 = Inc[_1]
  type _3 = Inc[_2]
  type _4 = Inc[_3]
  type _5 = Inc[_4]
  type _6 = Inc[_5]
  type _7 = Inc[_6]
  type _8 = Inc[_7]
  type _9 = Inc[_8]
  type _10 = Inc[_9]
  type _11 = Inc[_10]
  type _12 = Inc[_11]
  type _13 = Inc[_12]
  type _14 = Inc[_13]
  type _15 = Inc[_14]
  type _16 = Inc[_15]
  type _17 = Inc[_16]
  type _18 = Inc[_17]
  type _19 = Inc[_18]
  type _20 = Inc[_19]
  type _21 = Inc[_20]
  type _22 = Inc[_21]
  type _23 = Inc[_22]
  type _24 = Inc[_23]
  type _25 = Inc[_24]
}
