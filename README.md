# ∀ Linear Algebra ∃ Proof System between Types

**shapesafe** is the one-size-fits-all compile-time verifier for numerical linear algebra on JVM, obvious shape and indexing errors in tensor operations are captured by scala's typing system.

shapesafe actively proves itself while being written. The following capabilities are introduced at inception:

##### static & runtime-dependent tensor shapes of arbitrary rank

![S1](doc/video/S1.gif)

##### named tensor: each dimension is indexed by both its name and ordinal number

![S2](doc/video/S2.gif)

##### tensor contractions & operations that depends on index equality, (all cases of EinSum, dot/cross/matrix/hadamard product)

![S3](doc/video/S3.gif)

##### operations that depends on shape arithmetics (convolution, direct sum, kronecker product, flatten/reshape)

![S4](doc/video/S4.gif)

##### complex function composition, with no implicit scope 

![S5](doc/video/S5.gif)

(the above examples are generated by compiling [shapesafe-demo](https://github.com/tribbloid/shapesafe-demo/tree/main/src/shouldSucceed/scala/org/shapesafe/demo/core/c1) in Visual Studio Code + Scala (Metals) plugin)

**It is not a tensor computing library!** Instead, it is designed to be embedded into existing libraries to enable less error-prone prototyping (see Roadmap for possible augmentations).

shapesafe started as an assignment to understand intuitionistic type theory used in compiler design, it minimally depends on [singleton-ops](https://github.com/fthomas/singleton-ops) and [shapeless](https://github.com/milessabin/shapeless).

Support for scala-2.13 is always guaranteed, supports for scala-2.12 & scala-js will only be enforced intermittently and upon request, please create (or vote for) tickets to backport for a specific compiler.

### Build Status

| branch \ profile | Scala-2.12 | Scala-2.13 | Scala-2.13 w/ splain plugin |
| ---- | ---- | ---- | ---- |
| master | ![CI-legacy](https://github.com/tribbloid/shapesafe/workflows/CI-legacy/badge.svg?branch=master) | ![CI](https://github.com/tribbloid/shapesafe/workflows/CI/badge.svg?branch=master) | ![CI](https://github.com/tribbloid/shapesafe/workflows/CI-splain/badge.svg?branch=master) |
| (latest in-progress) | ![CI-legacy](https://github.com/tribbloid/shapesafe/workflows/CI-legacy/badge.svg) | ![CI](https://github.com/tribbloid/shapesafe/workflows/CI/badge.svg) | ![CI-legacy](https://github.com/tribbloid/shapesafe/workflows/CI-splain/badge.svg) |

### Roadmap

##### High priority

- DJL integration
- Symbolic reasoning for variable dimensions, using Ring/Field axioms and natural deduction

##### Low priority

- DL4j & ND4j integration
- breeze integration (only tensors to up to rank-2 is required)

### How to compile

In POSIX shell, run `./dev/make-all.sh`

Guaranteed to be working by [Continuous Integration](.github/workflows/main.yml)

You must have an installed JDK that supports Gradle 7+ before the compilation

### Architecture

Unlike most of its predecessors, tensor operations in shapesafe are **lazily verified**, and writing expressions requires no declaration of implicit type class.
This is a deliberate design which allows complex operand compositions to be defined with no boilerplate (see [example above](#complex-function-composition-with-no-implicit-scope)). As a trade-off, shape errors in expressions are suppressed when defined, at which time they are still represented as computation graph:

```scala
  val a = Shape(1, 2)
  val b = Shape(3, 4)
  val s1 = (a >< b)
    .named("i", "j")
  s1.peek

// [INFO] 1 >< 2 >< (3 >< 4) |<<- (i >< j)
```

The errors are only captured once the expression is evaluated (e.g. by explicitly calling `.eval` or `.reason`, which does `peek` and `eval` simultaneously), which summons all algebraic rules like a proof assistant:

```scala
  s1.eval

// [ERROR] Dimension mismatch
//
// ... when proving shape ░▒▓
//
// 1 >< 2 >< 3 >< 4 |<<- (i >< j)
```

In the above example, calling `eval` instructs the compiler to summon a series of type classes as lemmata to prove / refute the correctness of the expression:

|                                         lemma |       | expression                              |
| --------------------------------------------: | :---: | --------------------------------------- |
|                                               |       | (1 >< 2) **><** (3 >< 4) \|<<- (i >< j) |
|                         (prove outer product) |  =    | 1 >< 2 >< 3 >< 4 **\|<<-** (i >< j)     |
| (refute naming of tensor: Dimension mismatch) |  !    |                                         |

Evidently, `eval` can only be used *iff.* each shape operand in the expression (in the above example `a` and `b`)  is either already evaluated, or can be evaluated in the same scope. This is the only case when implicit arguments has to be declared by the user.

Thus, the entire shape algebra can be defined using only 2 types of expressions (and a bunch of rules to manipulate them):

- **Arity** - describing 1D vectors:

![Arity](doc/ArityTypeHierarchy.png)

- **Shape** - describing ND tensors:

![Shape](doc/ShapeTypeHierarchy.png)

Shapesafe works most efficiently if dimensions of all tensors are either constants (represented by `org.shapesafe.core.arity.Const`), or unchecked (represented by `org.shapesafe.core.arity.Unchecked`,  meaning that it has no constraint or symbol, and should be ignored in validation). In practice, this can reliably support the majority of applied linear algebra / ML use cases. Support for algebra of variable shapes (with symbol, represented by `org.shapesafe.core.arity.Var`) will be gradually enabled in future releases.

**For more about the idea behind this project**, check the following resources

- Slide: https://github.com/tribbloid/shapesafe-demo/raw/main/__presentations/SBTB2021/slide.pdf

##### Upgrade to Scala 3

Most features in shapeless & singleton-ops are taken over by native compiler features:

- shapeless.Witness → singleton type
- shapeless.Poly → polymorphic function
- singleton.ops.== → inline conditions & matches
- singleton.ops._ → scala.compiletime.ops.*
- shapeless.HList → Tuple
- shapeless.record → Programmatic Structural Types

... but some are still missing:

- Product to Tuple conversion, plus its variants:
  - shapeless.NatProductArgs
  - shapeless.SingletonProductArgs
- ecosystem: Apache Spark, CHISEL, LMS, typelevel stack, and much more

Scala 3/dotty appears to be vastly more capable as a "proof assistant", with 15~20x speed improvement over Scala 2 on implicit search. This seems to indicate that shapesafe could only achieve large scale, production-grade algebraic verification after the upgrade is finished. At this moment (with Scala 2.13), if the implicit search on your computer is too slow, consider breaking you big operand composition into multiple small ones, and evaluate in-between as often as possible.

### Credit

- [Prof. Dmytro Mitin](https://www.researchgate.net/profile/Dmytro-Mitin) at National Taras Shevchenko University of Kyiv
- All maintainers of [singleton-ops](https://github.com/fthomas/singleton-ops)
- All maintainers of [shapeless](https://github.com/milessabin/shapeless)
- All maintainers of [splain](https://github.com/tek/splain)
- Breandan Considine, maintainer of [Kotlin∇](https://openreview.net/forum?id=SkluMSZ08H)

This project is heavily influenced by Kotlin∇ (see discussion [here](https://github.com/breandan/kotlingrad/issues/11)) and several pioneers in type-safe ML:

- Evan Spark for [first showing the possibility](https://etrain.github.io/2015/05/28/type-safe-linear-algebra-in-scala)
- Tongfei Chen et al. for [Nexus](https://github.com/ctongfei/nexus)
- Dougal Maclaurin et al. for [Dex](https://github.com/google-research/dex-lang)
- Maxime Kjaer et al. for [tf-dotty](https://github.com/MaximeKjaer/tf-dotty)

