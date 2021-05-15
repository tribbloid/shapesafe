# ∀ Linear Algebra ∃ Proof System between Types

**shapesafe** is the missing compile-time verifier for numerical linear algebra on JVM, obvious shape and indexing errors in tensor operations are captured by scala's typing system.

shapesafe is one-size-fits-all. The following capabilities are introduced at inception:

- static & runtime-dependent tensor shapes of arbitrary rank

![S1](doc/video/S1.gif)

- named tensor: each dimension is indexed by both its name and ordinal number

![S2](doc/video/S2.gif)

- contractions & operations that depends on index equality, generally those representable by einsum notation (dot/cross/matrix/hadamard product)

![S3](doc/video/S3.gif)

- operations that depends on shape arithmetics (convolution, direct sum, kronecker product, flatten/reshape)

![S4](doc/video/S4.gif)


**It is not a tensor computing library!** Instead, it is designed to augment existing libraries for less error-prone prototyping (see Roadmap for possible augmentations).

shapesafe started as an assignment to understand Martin-Löf type theory used by scala compiler, it minimally depends on [singleton-ops](https://github.com/fthomas/singleton-ops) and [shapeless](https://github.com/milessabin/shapeless).

Support for scala-2.13 is always guaranteed, supports for scala-2.12 & scala-js will only be enforced intermittenly and upon request, please create (or vote for) tickets to backport for a specific major version.

### Build Status

| branch \ profile | scala-2.12 | scala-2.13
|---|---|---|
| master | ![CI-legacy](https://github.com/tribbloid/shapesafe/workflows/CI-legacy/badge.svg?branch=master) | ![CI](https://github.com/tribbloid/shapesafe/workflows/CI/badge.svg?branch=master) |
| (latest in-progress) | ![CI-legacy](https://github.com/tribbloid/shapesafe/workflows/CI-legacy/badge.svg) | ![CI](https://github.com/tribbloid/shapesafe/workflows/CI/badge.svg) |

### Roadmap

##### High priority

- DJL integration

##### Low priority

- DL4j & ND4j integration
- breeze integration (only tensors to up to rank-2 is required)

##### Upgrade to Scala 3 (dotty)

Most features in shapeless & singleton-ops are taken over by native compiler features:

- shapeless.Witness ==> singleton type
- shapeless.Poly ==> polymorphic function
- singleton.ops.== ==> inline conditions & matches
- singleton.ops._ ==> scala.compiletime.ops.*
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

### Credit

- [Prof. Dmytro Mitin](https://www.researchgate.net/profile/Dmytro-Mitin) at National Taras Shevchenko University of Kyiv
- All maintainers of [singleton-ops](https://github.com/fthomas/singleton-ops)
- All maintainers of [shapeless](https://github.com/milessabin/shapeless)
- All maintainers of [splain](https://github.com/tek/splain)
- Breandan Considine, lead committer of [Kotlin∇](https://openreview.net/forum?id=SkluMSZ08H)
