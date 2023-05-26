val vs: Versions = versions()

dependencies {

    api(project(":prover-commons:meta2"))
    testFixturesApi(testFixtures(project(":prover-commons:meta2")))
//    api("eu.timepit:refined_${vv.scala.binaryV}:0.9.14")
//    TODO: remove, most arity inspection macros doesn't work on collection/tuple, using shapeless Length as cheap alternative
}