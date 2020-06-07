#### Scala DSL

##### Purpose

To introduce a secondary tensor operation DSL (Domain Specific Language) written in & optimised for Scala language & various compilers (the most common of which are JVM based scalac 2.12+, other options are scalajs & dotty / Scala 3.x)

Despite not being a dependently typed language, Scala has the following advantages comparing to Java or Kotlin:

- Scala is the preferred language of Apache Spark, Apache Flink, CHISEL (Constructing Hardware In Scala Embedded Language) and NVidia RAPIDS, all of which are important infrastructures for large scale grid learning.
- Singleton types (shapeless / 2.13) allows arbitrary number of shape types to be generated on-demand, without relying on code generator or metaprogramming
- Path-dependent types allows shape algebra to be defined for both symbols & numbers
- Type inference by summoning implicits is more flexible for theorem proving than inheritance & delegation
- Operator overloading & infix syntax allows DSL to be closer to math notations, e.g. `vec1 dot_* vec2` can be easier to read than `dotProduct(vec1, vec2)`
- Advanced type algebra can be expressed in macro & incoming Scala experimental features. Specifically, the `singleton-ops` library and it’s associated discussions in SIP-23 have suggested that Scala 2.13 & 3.x will use a more direct & expressive approach

##### Features

A PoC project has been submitted as:



At this moment (06/07/2020) it only implemented very few operations for double vectors. However most features in the following list has been proven and can be showcased by running `DoubleVectorSpec.scala` using gradle test.

Required & proven features:

- Type-level shape representation for vector
- Compile-time shape safety for operations with constant shapes (1)
- Run-time shape safety for operations with variable shapes (2)
- (1) can smoothly degrade to (2) for more flexible programming paradigm
- Not relying on compile-time code generation or defining large number of compile-time classes, both of which may overload a JVM classloader
- Not relying on esoteric compiler plugin, unstable macro or unstable libraries. (My only dependency in the PoC is the `singleton-ops`)

Required & unproven feature:

- Type-level shape representation for matrix
- Type-level shape representation for arbitrary tensor, with no limitation on its arity and number of dimensions (this can be achieved using recursive generic types, the same technique being used for `shapeless.HList`)
- Support for uint & float element data types
- Support for more operations, particularly ANN related
- easily convertible to Apache Spark & NVidia RAPIDS execution plans for forward/backward

Nice to have:

- Not relying on church-encoded types (The only violation in my PoC is `shapeles.Nat`), which causes slow compilation



Not in scope:



