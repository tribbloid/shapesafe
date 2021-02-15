# The missing compile-time verifier for linear algebra

shapeless is a Scala library that aims to capture obviously incorrect tensor operations at compile-time.

**It is not a tensor computing library**. Instead, it is designed to latch on existing libraries for proof of correctness (see Roadmap for possible latching)

#### Build Status

| branch \ profile | scala-2.12 | scala-2.13
|---|---|---|
| master | ![CI-legacy](https://github.com/tribbloid/shapesafe/workflows/CI-legacy/badge.svg?branch=master) | ![CI](https://github.com/tribbloid/shapesafe/workflows/CI/badge.svg?branch=master) |

#### Roadmap

##### High priority

- DJL integration

##### Low priority

- DL4j & ND4j integration
- breeze integration (only tensors to up to rank-2 is required)

##### Upgrade to Scala 3 (dotty)

Most features in shapeless & singleton-ops are taken over by native compiler features:

- shapeless.Witness ==> singleton type
- singleton.ops.== ==> inline conditions & matches
- singleton.ops._ ==> scala.compiletime.ops
- shapeless.HList ==> Tuple

... but some are still missing:

- extensible & contractable Record type
  - shapeless.record => ???
  - used to derive output of EinSum & tensor contraction
  - proposed a long time ago: https://github.com/lampepfl/dotty-feature-requests/issues/8, but contraction is still problematic
- Product to Tuple conversion, plus its variants:
  - shapeless.NatProductArgs
  - shapeless.SingletonProductArgs
- ecosystem: Apache Spark, CHISEL, the typelevel cohort, and much more