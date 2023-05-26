val vs: Versions = versions()

dependencies {

    api(project(":prover-commons:meta2"))
    testFixturesApi(testFixtures(project(":prover-commons:meta2")))

    api("eu.timepit:singleton-ops_${vs.scala.binaryV}:0.5.2")
}