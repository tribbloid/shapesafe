val vs: Versions = versions()

dependencies {

    api(project(":prover-commons"))
    testFixturesApi(testFixtures(project(":prover-commons")))

    api("eu.timepit:singleton-ops_${vs.scalaBinaryV}:0.5.2")
}